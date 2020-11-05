package com.nicko.anticheat.update.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.nicko.anticheat.update.MovementUpdate;

public class RotationUpdate extends MovementUpdate {
    public RotationUpdate(Player player, Location to, Location from) {
        super(player, to, from);
    }
}
