package com.nicko.anticheat.check.impl.killaura;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.RotationCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.location.PlayerLocation;
import com.nicko.anticheat.update.impl.RotationUpdate;
import com.nicko.anticheat.util.math.MathUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class KillAuraF extends RotationCheck {
    private float previousPitchChange;
    private int vl;

    public KillAuraF(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Kill Aura§7[§cF§7]");
    }

    @Override
    public void run(Player player, RotationUpdate update) {
        if (this.playerData.getPlayerLocations().size() <= 10) {
            return;
        }

        PlayerLocation pastLocation = this.playerData.getPlayerLocations().get(this.playerData.getPlayerLocations().size() - MathUtil.timeToTicks(player) - 1),
                currentLocation = this.playerData.getPlayerLocations().get(this.playerData.getPlayerLocations().size() - MathUtil.timeToTicks(player));

        Location trueCurrentLocation = new Location(player.getWorld(), currentLocation.getX(), currentLocation.getY(), currentLocation.getZ(), currentLocation.getYaw(), currentLocation.getPitch()),
                truePastLocation = new Location(player.getWorld(), pastLocation.getX(), pastLocation.getY(), pastLocation.getZ(), pastLocation.getYaw(), pastLocation.getPitch());

        if (MathUtil.isUsingOptifine(trueCurrentLocation, truePastLocation)) {
            return;
        }

        float pitchChange = MathUtil.getDistanceBetweenAngles(trueCurrentLocation.getPitch(), truePastLocation.getPitch());
        float yawChange = MathUtil.getDistanceBetweenAngles(trueCurrentLocation.getYaw(), truePastLocation.getYaw());

        if (yawChange > 0.0f && pitchChange > 0.0) {
            long expandedPitch = (long) (pitchChange * MathUtil.EXPANDER);
            long expandedPreviousPitch = (long) (this.previousPitchChange * MathUtil.EXPANDER);

            if (MathUtil.getGcd(expandedPitch, expandedPreviousPitch) <= MathUtil.MIN_EXPANSION_VALUE) {
                if (++vl > 4) {
                    this.onViolation(player, "flaggou " + name);
                }
            } else {
                vl = Math.max(vl - 1, 0);
            }
        }

        this.previousPitchChange = pitchChange;
    }
}
