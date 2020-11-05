package com.nicko.anticheat.check.impl.aimassist;

import com.nicko.anticheat.check.checks.RotationCheck;
import com.nicko.anticheat.update.impl.RotationUpdate;
import lombok.val;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;

public class AimAssistA extends RotationCheck {
    private float lastPitchChange, lastYaw;

    public AimAssistA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Aim Assist§7[§cA§7]");
    }

    @Override
    public void run(Player player, RotationUpdate update) {
        val pitchChange = Math.abs(update.getTo().getPitch() - update.getFrom().getPitch()) % 180.0f;
        val yawChange = Math.abs(update.getTo().getYaw() - update.getFrom().getYaw()) % 180.0f;

        val pitchDifference = Math.abs(this.lastPitchChange - pitchChange);
        val yawDifference = Math.abs(this.lastYaw - yawChange);

        if (yawChange > yawDifference && yawDifference > 0.3 && pitchChange > 0 && pitchChange <= pitchDifference && pitchDifference < 0.1
        && this.onViolation(player, " flaggou " + name)) {
        }

        if (yawChange > 3.0 && pitchChange > 0.001F && pitchChange < 0.0094F && pitchDifference > 1.0 &&
                this.onViolation(player, "flaggou " + name)) {
        }

        if (pitchDifference > 0.01F && pitchDifference < 0.04F && pitchChange > 9.f
                && this.onViolation(player, "flaggou " + name)) {
        }

        this.lastYaw = yawChange;
        this.lastPitchChange = pitchChange;
    }
}
