package com.nicko.anticheat.update.impl;

import com.nicko.anticheat.update.MovementUpdate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RotationUpdate extends MovementUpdate {
    public RotationUpdate(Player player, Location to, Location from) {
        super(player, to, from);
    }
}
