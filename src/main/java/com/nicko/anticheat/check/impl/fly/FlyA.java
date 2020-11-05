package com.nicko.anticheat.check.impl.fly;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.PositionUpdate;
import org.bukkit.entity.Player;

public class FlyA extends PositionCheck {
    private int airTicks;

    public FlyA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Fly§7[§cA§7]");
    }

    @Override
    public void run(Player player, PositionUpdate update) {
        double dy = update.getTo().getY() - update.getFrom().getY();

        if (this.playerData.isWasOnLadder() || this.playerData.isOnLadder() || this.playerData.getLastLocation() == null ||
                this.playerData.getTeleportManager().hasTeleported(this.playerData.getLastLocation(), 3.0)
                || this.playerData.isWasInLiquid() || this.playerData.getVelocityY() > 0.0 || this.playerData.isOnGround()) {
            this.airTicks = 0;
            return;
        }

        if (this.playerData.isBelowBlock() || this.playerData.isWasBelowBlock()) {
            return;
        }

        if (dy < 0.0) {
            this.airTicks = 0;
        }

        if (dy > 0.0 && ++airTicks == 1 && dy < 9.0E-15) {
            this.onViolation(player, "flaggou " + name);
        }
    }
}
