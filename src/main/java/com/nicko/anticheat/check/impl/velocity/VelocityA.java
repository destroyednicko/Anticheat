package com.nicko.anticheat.check.impl.velocity;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.PositionUpdate;
import com.nicko.anticheat.util.nms.NmsUtil;
import org.bukkit.entity.Player;

public class VelocityA extends PositionCheck {
    private long lastMotion;

    public VelocityA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Velocity§7[§cA§7]");
    }

    @Override
    public void run(Player player, PositionUpdate update) {
        if (this.playerData.getVelocityY() > 0.0 && !this.playerData.isBelowBlock() && !this.playerData.isWasBelowBlock() &&
                !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() &&
                !this.playerData.isInWeb() && !this.playerData.isWasInWeb()) {
            this.lastMotion += 50L;

            if (this.lastMotion > 500L + NmsUtil.getNmsPlayer(player).ping * 2L) {
                this.onViolation(player, "flaggou " + name);

                //this.ban(player, name, 5);
                this.playerData.setVelocityY(0.0);
                this.lastMotion = 0L;
            }
        } else {
            this.lastMotion = 0L;
        }
    }
}
