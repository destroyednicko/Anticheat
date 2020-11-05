package com.nicko.anticheat.check.impl.badpackets;

import com.nicko.anticheat.check.checks.PacketCheck;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;

public class BadPacketsA extends PacketCheck {
    private int flyingPacketsInARow;

    public BadPacketsA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Bad Packets§7[§cA§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (player.getVehicle() != null) {
                this.flyingPacketsInARow = 0;
            }

            if (packet.getClass() == PacketPlayInFlying.class) {
                if (++flyingPacketsInARow > 20) {
                    this.onViolation(player, "flaggou " + name);
                }
            } else {
                flyingPacketsInARow = 0;
            }
        }
    }
}
