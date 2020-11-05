package com.nicko.anticheat.check.impl.badpackets;

import com.nicko.anticheat.check.checks.PostCheck;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;

public class BadPacketsC extends PostCheck {
    public BadPacketsC(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, PacketPlayInArmAnimation.class, "Bad Packets§7[§cC§7]");
    }

    private int vl;

    @Override
    public void run(Player player, Packet packet) {
        if ((packet instanceof PacketPlayInArmAnimation || packet instanceof PacketPlayInFlying) && !this.playerData.getActionManager().isDigging()) {
            boolean isLagging = System.currentTimeMillis() - this.playerData.getLastDelayedPacket() <= 160L;

            if (!isLagging && isPost(packet)) {
                if (vl++ >= 1) {
                    onViolation(player, "flaggou " + name);
                }
            } else if (vl > 0) {
                vl = 0;
            }
        }
    }
}
