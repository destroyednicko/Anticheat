package com.nicko.anticheat.update.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.nicko.anticheat.update.MovementUpdate;

public class PositionUpdate extends MovementUpdate {
    public PositionUpdate(Player player, Location to, Location from) {
        super(player, to, from);
    }
}
