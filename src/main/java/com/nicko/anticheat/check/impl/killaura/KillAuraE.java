package com.nicko.anticheat.check.impl.killaura;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;
import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class KillAuraE extends PacketCheck {

    public KillAuraE(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData,"Kill Aura§7[§cE§7]");
    }

    private long lastPreAction;
    private double vl;

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            val lastLocation = this.playerData.getLastLocation();
            val now = System.currentTimeMillis();

            if (lastLocation == null || now - this.playerData.getLastDelayedPacket() <= 160L) {
                return;
            }

            if (now - lastLocation.getTimestamp() + 20 > 70) {
                this.lastPreAction = now;
                vl += now - lastLocation.getTimestamp() + 20 > 80 ? 2 : 0;
            } else if (vl > 0) {
                vl /= 2;
            }
        } else if (packet instanceof PacketPlayInFlying) {
            val now = System.currentTimeMillis();

            if (this.lastPreAction + 100L > now) {
                if (vl++ > 5) {
                    onViolation(player, "flaggou " + name);
                }
            } else if (vl > 0) {
                vl = 0;
            }
        }
    }
}
