package com.nicko.anticheat.check.impl.badpackets;

import com.nicko.anticheat.check.checks.PacketCheck;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;

public class BadPacketsB extends PacketCheck {
    public BadPacketsB(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Bad Packets§7[§cB§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying && Math.abs(((PacketPlayInFlying) packet).e()) > 90.f &&
                this.onViolation(player, "flaggou " + name)) {
            //this.ban(player, name, 0);
        }
    }
}
