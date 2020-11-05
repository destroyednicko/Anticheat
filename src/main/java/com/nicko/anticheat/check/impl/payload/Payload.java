package com.nicko.anticheat.check.impl.payload;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.Check;
import com.nicko.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

public class Payload extends Check<String> {
    public Payload(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, String.class, "Payload§7[§cA§7]");
    }

    @Override
    public void run(Player player, String p) {

    }
}
