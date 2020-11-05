package com.nicko.anticheat.check.impl.aimassist;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.RotationCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.RotationUpdate;
import com.nicko.anticheat.util.math.MathUtil;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AimAssistB extends RotationCheck {

    public AimAssistB(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Aim Assist§7[§cB§7]");
    }

    private int vl;
    private float lastYawChange, lastPitchChange;
    private double previousPitchSquaredInversed;

    @Override
    public void run(Player player, RotationUpdate update) {
        Location from = update.getFrom(),
                to = update.getTo();

        val now = System.currentTimeMillis();

        val yawChange = Math.abs(to.getYaw() - from.getYaw()) % 180.0f;
        val pitchChange = Math.abs(to.getPitch() - from.getPitch()) % 180.0f;

        val yawDifference = Math.abs(this.lastYawChange - yawChange);
        val pitchDifference = Math.abs(this.lastPitchChange - pitchChange);

        val pitchSquaredInverse = MathUtil.invSqrt(pitchDifference);
        val yawSquaredInverse = MathUtil.invSqrt(yawDifference);

        if (now - this.playerData.getActionManager().getLastAttack() < 700L) {
            if (yawSquaredInverse != pitchSquaredInverse) {
                if (pitchSquaredInverse == previousPitchSquaredInversed) {
                    if (vl++ >= 1) {
                        onViolation(player, "flaggou " + name);
                    }
                } else if (vl > 0) {
                    vl = Math.max(vl - 1, 0);
                }
                this.previousPitchSquaredInversed = pitchSquaredInverse;
            } else if (vl > 0) {
                vl = 0;
            }
        }
        lastYawChange = yawChange;
        lastPitchChange = pitchChange;
    }
}
