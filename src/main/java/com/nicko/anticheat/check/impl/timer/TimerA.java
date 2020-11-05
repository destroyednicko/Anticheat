package com.nicko.anticheat.check.impl.timer;

import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.util.stats.MovingStats;

public class TimerA extends PacketCheck {
    private long lastFlyingTime;

    private final MovingStats movingStats = new MovingStats(20);

    public TimerA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Timer§7[§cA§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            val now = System.currentTimeMillis();
            val lastLocation = playerData.getLastLocation();

            if (lastLocation == null) {
                return;
            }

            if (this.playerData.getTeleportManager().hasTeleported(lastLocation, 3.0) ||
                    System.currentTimeMillis() - this.playerData.getLastJoinTime() <= 3000) {
                return;
            }

            movingStats.add(now - this.lastFlyingTime);

            val max = 7.07;
            val stdDev = movingStats.getStdDev(max);

            if (stdDev != 0.0E00 / 0.0E00 && stdDev < max) {
                Bukkit.broadcastMessage(String.valueOf(now - this.lastFlyingTime));
                this.onViolation(player, "flaggou " + name);
            }


            this.lastFlyingTime = now;
        }
    }
}
