package com.nicko.anticheat.check.checks;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.Check;
import com.nicko.anticheat.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

public class PacketCheck extends Check<Packet> {
    public PacketCheck(AnticheatPlugin plugin, PlayerData playerData, String name) {
        super(plugin, playerData, Packet.class, name);
    }

    @Override
    public void run(Player player, Packet packet) {

    }
}
