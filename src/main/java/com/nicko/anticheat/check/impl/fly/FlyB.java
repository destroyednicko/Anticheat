package com.nicko.anticheat.check.impl.fly;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.PositionUpdate;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlyB extends PositionCheck {

    private double jumpLimit;
    private double jumpMultiplier;
    private double fallSpeedLimit;
    private int fallViolations, ascendTicks;

    public FlyB(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Fly§7[§cB§7]");
    }

    @Override
    public void run(Player player, PositionUpdate update) {
        val from = update.getFrom();
        val to = update.getTo();

        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) {
            return;
        }

        val dy = to.getY() - from.getY();

        if (player.isFlying() || player.isInsideVehicle() || this.playerData.isOnGround() || this.playerData.isInLiquid() || this.playerData.isOnLadder()) {
            this.resetLimits(player);
            return;
        }

        if (this.playerData.isWasOnGround()) {
            this.resetLimits(player);
        }

        if (this.playerData.isWasInLiquid()) {
            this.jumpLimit += 0.18;
        }

        if (dy > 0.0) {
            if (dy > this.jumpLimit + this.playerData.getVelocityManager().getMaxVertical()) {
                this.onViolation(player, "flaggou " + name);
                this.jumpLimit *= this.jumpMultiplier;
                this.jumpMultiplier -= 0.025;
            }
            if (ascendTicks++ > 5) {
                onViolation(player, "flaggou " + name);
            }
        } else if (dy < 0.0) {
            if (this.fallSpeedLimit - dy < 0.2 && ++this.fallViolations == 6) {
                this.onViolation(player, "flaggou " + name);
                this.resetLimits(player);
            }

            this.fallSpeedLimit -= 0.01;
        } else if (dy == 0.0 && ++this.fallViolations == 5) {
            this.onViolation(player, "flaggou " + name);
            this.resetLimits(player);
        }
    }

    private void resetLimits(Player player) {
        val jumpAmplifier = this.getJumpBoostAmplifier(player);
        this.jumpMultiplier = 0.8 + 0.03 * jumpAmplifier;
        this.jumpLimit = 0.42 + 0.11 * jumpAmplifier;
        this.fallSpeedLimit = 0.078;
        this.fallViolations = 0;
        this.ascendTicks = 0;
    }

    private int getJumpBoostAmplifier(Player player) {
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.JUMP)) {
                    return effect.getAmplifier() + 1;
                }
            }
        }
        return 0;
    }
}