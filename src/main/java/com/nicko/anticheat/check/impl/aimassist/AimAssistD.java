package com.nicko.anticheat.check.impl.aimassist;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.RotationCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.RotationUpdate;
import com.nicko.anticheat.util.math.MathUtil;
import lombok.val;
import org.bukkit.entity.Player;

public class AimAssistD extends RotationCheck {
    private float suspiciousYaw;

    public AimAssistD(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Aim Assist§7[§cD§7]");
    }

    @Override
    public void run(Player player, RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getActionManager().getLastAttack() > 10000L) {
            return;
        }

        val yawChange = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        if (yawChange > 1.0f && Math.round(yawChange * 10.0f) * 0.1f == yawChange && Math.round(yawChange) != yawChange && yawChange % 1.5f != 0.0f) {
            if (yawChange == this.suspiciousYaw && this.onViolation(player, "flaggou " + name)) {
                //this.ban(player, name, 20);
            }
            this.suspiciousYaw = Math.round(yawChange * 10.0f) * 0.1f;
        } else {
            this.suspiciousYaw = 0.0f;
        }

    }
}