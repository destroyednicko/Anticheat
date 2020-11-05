package com.nicko.anticheat.check.impl.invalidmove;

import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.update.impl.PositionUpdate;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;

public class InvalidMoveA extends PositionCheck {

    public InvalidMoveA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Invalid Move§7[§cA§7]");
    }

    private int speedThreshold;

    @Override
    public void run(Player player, PositionUpdate update) {
        double deltaY = update.getTo().getY() - update.getFrom().getY();

        if (player.isFlying() || player.getGameMode() == GameMode.CREATIVE || this.playerData.isBelowBlock() ||
                this.playerData.isWasBelowBlock() || this.playerData.isInLiquid() || this.playerData.isWasInLiquid()
                || this.playerData.getVelocityY() > 0.0 || this.playerData.getVelocityX() > 0.0 || this.playerData.getVelocityZ() > 0.0) {
            return;
        }

        if (update.getFrom().getY() % 1.0 == 0 && update.getTo().getY() % 1.0 > 0) {
            if (deltaY < 0.41999998688697815) {
                if (speedThreshold++ > 5) {
                    this.onViolation(player, "flaggou " + super.name);
                }
            } else speedThreshold = 1;
        }
    }
}
