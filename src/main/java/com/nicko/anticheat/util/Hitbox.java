package com.nicko.anticheat.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Hitbox {
    private final Location aa, ab;

    public Hitbox(Player player){
        this.aa = player.getLocation().subtract(0.4, 0, 0.4);
        this.ab = player.getLocation().add(0.4, 1.8, 0.4);
    }
    public Hitbox(Location aa, Location ab){
        this.aa = aa;
        this.ab = ab;
    }

    public boolean intersects(Location l){
        return ((l.getX() <= aa.getX() && l.getY() <= aa.getY() && l.getZ() <= aa.getZ())
                && (l.getX() >= ab.getX() && l.getY() >= ab.getY() && l.getZ() >= ab.getZ()));
    }

    public Location getPointAA(){
        return aa;
    }

    public Location getPointAB(){
        return ab;
    }
}
