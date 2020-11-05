package com.nicko.anticheat.check.impl.jesus;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.PositionUpdate;
import com.nicko.anticheat.util.location.MaterialUtil;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.LinkedList;

public class JesusA extends PositionCheck {

    public JesusA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Jesus§7[§cA§7]");
    }

    private int vl, secondVl;
    private double previousDeltaY;
    private long lastFlag;

    private final Deque<Long> flagSpeedList = new LinkedList<>();

    @Override
    public void run(Player player, PositionUpdate update) {
        Location from = update.getFrom().clone(),
                to = update.getTo().clone();

        val deltaY = to.getY() - from.getY();
        val now = System.currentTimeMillis();

        boolean onSolid = MaterialUtil.checkFlag(to.subtract(0, 0.3, 0).getBlock().getType(), 1);

        if (this.playerData.isWasInLiquid() && !onSolid) {
            if (this.previousDeltaY % deltaY == 0.0 || Double.isNaN(this.previousDeltaY % deltaY)) {
                if (vl++ > 8) {
                    onViolation(player, "flaggou " + name);
                }
                lastFlag = now;
            } else if (vl > 0) {
                vl = 0;
            }

            flagSpeedList.add(now - lastFlag);

            if (flagSpeedList.size() == 7 ) {
                double average = flagSpeedList.stream()
                        .mapToDouble(d -> d)
                        .average()
                        .orElse(0.0);

                if (average == 0.0) {
                    onViolation(player, "flaggou " + name);
                }

                flagSpeedList.clear();
            }

            previousDeltaY = deltaY;
        } else if (vl > 0) {
            vl = 0;
        }
    }
}
