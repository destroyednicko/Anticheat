package com.nicko.anticheat.handler;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.update.impl.PositionUpdate;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MovementHandler {

    private final AnticheatPlugin plugin;

    public void handleLocationUpdate(Player player, Location to, Location from) {
        if (player.getAllowFlight() || player.isInsideVehicle() || to.getY() < 4.0) {
            return;
        }

        if (!player.getWorld().isChunkLoaded(to.getBlockX() >> 4, to.getBlockZ() >> 4)) {
            return;
        }

        val playerData = this.plugin.getData(player);

        if (playerData == null) {
            return;
        }

        // atualizar os flags
        playerData.getMovementParser().updatePositionFlags(player, to);

        playerData.getCheckList().stream()
                .filter(PositionCheck.class::isInstance)
                .forEach(check -> check.run(player, new PositionUpdate(player, to, from)));
    }
}
