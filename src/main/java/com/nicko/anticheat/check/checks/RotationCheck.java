package com.nicko.anticheat.check.checks;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.Check;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.RotationUpdate;
import org.bukkit.entity.Player;

public class RotationCheck extends Check<RotationUpdate> {
    public RotationCheck(AnticheatPlugin plugin, PlayerData playerData, String name) {
        super(plugin, playerData, RotationUpdate.class, name);
    }

    @Override
    public void run(Player player, RotationUpdate update) {

    }
}
