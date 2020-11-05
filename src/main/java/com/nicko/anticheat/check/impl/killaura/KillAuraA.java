package com.nicko.anticheat.check.impl.killaura;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;
import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraA extends PacketCheck {
    private long lastAttack;
    private double vl, secondVl, thirdVl;

    public KillAuraA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Kill Aura§7[§cA§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInUseEntity &&
                ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            val lastLocation = playerData.getLastLocation();

            if (lastLocation == null) {
                return;
            }

            val now = System.currentTimeMillis();
            boolean isLagging = now - this.playerData.getLastDelayedPacket() <= 160L;

            if (!isLagging && now - lastLocation.getTimestamp() < 20) {
                if (secondVl++ > 3) {
                    onViolation(player, "flaggou " + name);
                }
            } else if (secondVl > 0) {
                secondVl = Math.max(secondVl - 1, 0);
            }

            if (!isLagging && lastLocation.getTimestamp() + 20L > now) {
                this.lastAttack = now;
            } else if (vl > 0) {
                vl -= 0.5;
            }
        } else if (packet instanceof PacketPlayInFlying) {
            val now = System.currentTimeMillis();
            boolean isLagging = now - this.playerData.getLastDelayedPacket() <= 160L;

            if (!isLagging && this.lastAttack + 100L > now) {
                if (++vl > 4) {
                    this.onViolation(player, "flaggou " + name);
                }
            } else if (vl > 0){
                vl -= 0.5;
            }
        }
    }
}
