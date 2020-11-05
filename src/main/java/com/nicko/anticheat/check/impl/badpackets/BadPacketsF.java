package com.nicko.anticheat.check.impl.badpackets;

import com.nicko.anticheat.check.checks.PacketCheck;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;

public class BadPacketsF extends PacketCheck {
    public BadPacketsF(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Bad Packets§7[§cF§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInHeldItemSlot && this.playerData.getActionManager().isPlacing() &&
                this.onViolation(player, "flaggou " + name)) {
            //this.ban(player, name, 2);
        }
    }
}
