package com.nicko.anticheat.check.impl.reach;

import lombok.val;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.location.PlayerLocation;
import com.nicko.anticheat.util.math.MathUtil;
import com.nicko.anticheat.util.nms.NmsUtil;

public class ReachA extends PacketCheck {

    public ReachA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Reach§7[§cA§7]");
        this.plugin = plugin;
    }

    private AnticheatPlugin plugin;

    private int vl;

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            EntityPlayer nmsPlayer = NmsUtil.getNmsPlayer(player);

            Entity entity = ((PacketPlayInUseEntity) packet).a(nmsPlayer.world);
            org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();

            if (bukkitEntity instanceof Player) {
                Player target = (Player) bukkitEntity;

                if (System.currentTimeMillis() - this.playerData.getLastDelayedPacket() <= 160L || System.currentTimeMillis() - this.plugin.getData(target).getLastDelayedPacket() <= 160L
                        || this.playerData.getPlayerLocations().size() <= 10 || this.plugin.getData(target).getPlayerLocations().size() <= 10) {
                    return;
                }

                PlayerLocation targetLocation = this.plugin.getData(target).getPlayerLocations().get(this.plugin.getData(target).getPlayerLocations().size() - MathUtil.timeToTicks(target)),
                        playerLocation = this.playerData.getPlayerLocations().get(this.playerData.getPlayerLocations().size() - MathUtil.timeToTicks(player));

                if (targetLocation == null || playerLocation == null) {
                    return;
                }

                Location attackerLocation = new Location(player.getWorld(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
                Location attackedLocation = new Location(target.getWorld(), targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
                /*
                TODO: Completar o check. Pra fazer detectar 3.1.
                Pode dar falso-positivo porque o cálculo da distância ainda não foi terminad. Tenho que pensar em formas de deixar mais preciso.
                 */

                double reach = attackedLocation.toVector().setY(0).distance(attackerLocation.toVector().setY(0));
                val kbxz = Math.hypot(this.playerData.getVelocityX(), this.playerData.getVelocityZ());

                reach -= kbxz;
                reach -= MathUtil.getHitboxSize(attackerLocation.getYaw());
                reach -= this.playerData.getPlayerMoveSpeed();

                if (reach > 3.15) {
                    if (vl++ > 1) {
                        onViolation(player, "flaggou " + name);
                    }
                } else {
                    if (vl > 0) {
                        vl = Math.max(vl - 1, 0);
                    }
                }
            }
        }
    }
}
