package com.nicko.anticheat.check.impl.velocity;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.PositionUpdate;
import com.nicko.anticheat.util.nms.NmsUtil;
import lombok.val;
import org.bukkit.entity.Player;

public class VelocityC extends PositionCheck {
    private double vl;

    public VelocityC(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Velocity§7[§cA§7]");
    }

    @Override
    public void run(Player player, PositionUpdate update) {
        val dy = update.getTo().getY() - update.getFrom().getY();
        val dxz = Math.hypot(update.getTo().getX() - update.getFrom().getX(),
                update.getTo().getZ() - update.getFrom().getZ());

        val kbxz = Math.hypot(this.playerData.getVelocityX(), this.playerData.getVelocityZ());

        val entityPlayer = NmsUtil.getNmsPlayer(player);

        if (this.playerData.getVelocityY() > 0.0 && this.playerData.isWasOnGround() &&
                !this.playerData.isBelowBlock() && !this.playerData.isWasBelowBlock() && !this.playerData.isInLiquid() &&
                !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() &&
                update.getFrom().getY() % 1.0 == 0.0 && dy > 0.0 && dy < 0.41999998688697815 &&
                kbxz > 0.45 && !entityPlayer.world.c(entityPlayer.getBoundingBox().grow(1.0, 0.0, 1.0))) {

            val quotient = dxz / kbxz;

            if (quotient < 0.6) {
                if ((vl += 1.1) >= 8.0 && this.onViolation(player, name) && vl >= 15) {
                    // this.ban(player, name, 5);
                }
            } else {
                vl = Math.max(0, vl - 0.4);
            }
        }
    }
}
