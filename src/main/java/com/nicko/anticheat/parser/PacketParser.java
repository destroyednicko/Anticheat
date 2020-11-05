package com.nicko.anticheat.parser;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.check.checks.RotationCheck;
import com.nicko.anticheat.check.impl.payload.Payload;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.location.PlayerLocation;
import com.nicko.anticheat.update.impl.RotationUpdate;
import com.nicko.anticheat.util.ReflectionUtil;
import com.nicko.anticheat.util.location.LocationUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class PacketParser extends ChannelDuplexHandler {
    private final AnticheatPlugin plugin;
    private final Player player;
    private final PlayerData playerData;

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        val packet = (Packet) msg;

        if (packet instanceof PacketPlayInFlying) {
            val flying = (PacketPlayInFlying) packet;
            val now = System.currentTimeMillis();

            if (now - this.playerData.getLastFlying() >= 110L) {
                this.playerData.setLastDelayedPacket(now);
            }
            this.playerData.setLastFlying(now);

            val location = new PlayerLocation(flying.a(), flying.b(), flying.c(),
                    flying.d(), flying.e());
            val lastLocation = this.playerData.getLastLocation();

            if (lastLocation != null) {
                if (!flying.g()) {
                    location.setX(lastLocation.getX());
                    location.setY(lastLocation.getY());
                    location.setZ(lastLocation.getZ());
                }

                if (!flying.h()) {
                    location.setYaw(lastLocation.getYaw());
                    location.setPitch(lastLocation.getPitch());
                } else {
                    //noinspection unchecked
                    this.playerData.getCheckList().stream()
                            .filter(RotationCheck.class::isInstance)
                            .forEach(check -> check.run(player, new RotationUpdate(player,
                                    player.getLocation(), new Location(player.getWorld(),
                                    location.getX(), location.getY(), location.getZ()))));
                }
            }

            // previne qualquer bug desnecessário
            if (this.playerData.getPlayerLocations().size() > 50) {
                this.playerData.getPlayerLocations().clear();
            }
            if (this.playerData.getPlayerLocations().size() > 20) {
                this.playerData.getPlayerLocations().remove(0);
            }

            this.playerData.setLastLocation(location);
            this.playerData.getPlayerLocations().add(location);

            this.playerData.getActionManager().onFlying();
        } else if (packet instanceof PacketPlayInUseEntity) {
            if (((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.playerData.getActionManager().onAttack();
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            this.playerData.getActionManager().onArmAnimation();
        } else if (packet instanceof PacketPlayInEntityAction) {
            switch (((PacketPlayInEntityAction) packet).b()) {
                case START_SPRINTING:
                    this.playerData.setSprinting(true);
                    break;

                case STOP_SPRINTING:
                    this.playerData.setSprinting(false);
                    break;
            }
        } else if (packet instanceof PacketPlayInBlockPlace) {
            this.playerData.getActionManager().onPlace();
        } else if (packet instanceof PacketPlayInCustomPayload) {
            if (this.plugin.getClientManager().isCheating(player, this.playerData, ((PacketPlayInCustomPayload) packet).a())) {
                this.playerData.getCheck(Payload.class).onViolation(player, "Conectou com um cliente alternativo");
                Bukkit.broadcast("§c • §f " + player.getDisplayName() + " §cconectou com um Cliente Alternativo.", "anticheat.alerta");
            }
        }

        this.playerData.getCheckList().stream()
                .filter(PacketCheck.class::isInstance)
                .forEach(check -> check.run(player, packet));

        super.channelRead(channelHandlerContext, msg);
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object msg, ChannelPromise channelPromise) throws Exception {
        val packet = (Packet) msg;

        if (packet instanceof PacketPlayOutPosition) {
            val clazz = PacketPlayOutPosition.class;

            this.playerData.getTeleportManager().add(ReflectionUtil.getFieldValue(clazz,
                    "a", double.class, packet),

                    (double) ReflectionUtil.getFieldValue(clazz, "b", double.class, packet)
                            - 1.62f,

                    ReflectionUtil.getFieldValue(clazz, "c", double.class, packet));
        } else if (packet instanceof PacketPlayOutEntityVelocity) {
            val clazz = PacketPlayOutEntityVelocity.class;
            int id = ReflectionUtil.getFieldValue(clazz, "a", int.class, packet);

            if (id == player.getEntityId()) {
                val x = Math.abs(ReflectionUtil.getFieldValue(clazz,
                        "b", int.class, packet)) / 8000.0;

                val y = ((int) ReflectionUtil.getFieldValue(clazz,
                        "c", int.class, packet)) / 8000.0;

                val z = Math.abs(ReflectionUtil.getFieldValue(clazz,
                        "d", int.class, packet)) / 8000.0;

                if (y > 0.0 && this.playerData.isOnGround() && LocationUtil.isUneven(player.getLocation().getY())) {
                    this.playerData.setVelocityX(x);
                    this.playerData.setVelocityY(y);
                    this.playerData.setVelocityZ(z);
                }

                this.playerData.getVelocityManager().addVelocityEntry(x, y, z);
            }
        }

        super.write(channelHandlerContext, msg, channelPromise);
    }
}
