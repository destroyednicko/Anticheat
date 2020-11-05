package com.nicko.anticheat.parser;

import com.nicko.anticheat.util.location.MaterialUtil;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MovementParser {

    public boolean onGround;
    public boolean inLiquid;
    public boolean onLadder;
    public boolean inWeb;
    public boolean wasWeb;
    public boolean belowBlock;
    public boolean wasOnGround;
    public boolean wasInLiquid;
    public boolean wasOnLadder;
    public boolean wasBelowBlock;

    public void updatePositionFlags(Player player, Location to) {
        val world = player.getWorld();

        val startX = Location.locToBlock(to.getX() - 0.3);
        val endX = Location.locToBlock(to.getX() + 0.3);

        val startZ = Location.locToBlock(to.getZ() - 0.3);
        val endZ = Location.locToBlock(to.getZ() + 0.3);

        this.wasOnGround = this.onGround;
        this.wasInLiquid = this.inLiquid;
        this.wasOnLadder = this.onLadder;
        this.wasBelowBlock = this.belowBlock;
        this.wasWeb = this.inWeb;

        this.inWeb = false;
        this.onGround = false;
        this.inLiquid = false;
        this.onLadder = false;
        this.belowBlock = false;

        for (int bx = startX; bx <= endX; ++bx) {
            for (int bz = startZ; bz <= endZ; ++bz) {
                val by = Location.locToBlock(to.getY());

                val typeBelow = world.getBlockAt(bx, by - 1, bz).getType();
                val typeFeet = world.getBlockAt(bx, by, bz).getType();
                val typeHead = world.getBlockAt(bx, by + 1, bz).getType();
                val typeAbove = world.getBlockAt(bx, by + 2, bz).getType();

                if (MaterialUtil.checkFlag(typeFeet, MaterialUtil.LIQUID) ||
                        MaterialUtil.checkFlag(typeHead, MaterialUtil.LIQUID)) {
                    this.inLiquid = true;
                } else if (MaterialUtil.checkFlag(typeFeet, MaterialUtil.SOLID)) {
                    this.onGround = true;
                } else if (MaterialUtil.checkFlag(typeBelow, MaterialUtil.SOLID)) {
                    val distance = to.getY() - to.getBlockY();

                    if (distance < 0.2 || (MaterialUtil.checkFlag(typeBelow, MaterialUtil.WALL) && distance < 0.51)) {
                        this.onGround = true;
                    }
                } else if (MaterialUtil.checkFlag(typeFeet, MaterialUtil.COBWEB) ||
                        MaterialUtil.checkFlag(typeHead, MaterialUtil.COBWEB)) {
                    this.inWeb = true;
                }

                if (MaterialUtil.checkFlag(typeFeet, MaterialUtil.LADDER) ||
                        MaterialUtil.checkFlag(typeHead, MaterialUtil.LADDER)) {
                    this.onLadder = true;
                }

                // Adicionado isso para corrigir algumas flags falsas pro trapdoor
                if (MaterialUtil.checkFlag(typeAbove, MaterialUtil.SOLID) || MaterialUtil.checkFlag(typeHead, MaterialUtil.SOLID)) {
                    this.belowBlock = true;
                }
            }
        }
    }
}
