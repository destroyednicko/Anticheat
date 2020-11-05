package com.nicko.anticheat.check.checks;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.Check;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.PositionUpdate;
import org.bukkit.entity.Player;

public class PositionCheck extends Check<PositionUpdate> {
    public PositionCheck(AnticheatPlugin plugin, PlayerData playerData, String name) {
        super(plugin, playerData, PositionUpdate.class, name);
    }

    @Override
    public void run(Player player, PositionUpdate update) {
    }
}
