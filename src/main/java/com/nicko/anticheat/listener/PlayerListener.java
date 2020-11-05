package com.nicko.anticheat.listener;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.event.PlayerViolationEvent;
import com.nicko.anticheat.parser.PacketParser;
import com.nicko.anticheat.util.location.LocationUtil;
import com.nicko.anticheat.util.nms.NmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final AnticheatPlugin plugin;

    //TODO: adicionar listeners separados
    //TODO: adicionar https://google.github.io/guava/releases/19.0/api/docs/index.html?com/google/common/util/concurrent/RateLimiter.html
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();
        this.plugin.getData(player).setLastJoinTime(System.currentTimeMillis());

        val nmsPlayer = NmsUtil.getNmsPlayer(player);

        this.plugin.getPacketThread().execute(() -> nmsPlayer.playerConnection.networkManager.channel.pipeline()
                .addBefore("packet_handler",
                        "lancer_packet_handler",
                        new PacketParser(this.plugin, player, this.plugin.getData(player))));

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        val player = event.getPlayer();

        val from = event.getFrom();
        val to = event.getTo();

        if (from.distanceSquared(to) == 0) {
            return;
        }

        // talvez cê não goste disso, mas...
        // pode alterar se quiser >;v.
        val moveSpeed = LocationUtil.offset(from.toVector(), to.toVector());

        this.plugin.getMovementHandler().handleLocationUpdate(player, to, from);

        val playerData = this.plugin.getData(player);

        if (playerData.getVelocityY() > 0.0 && to.getY() > from.getY()) {
            playerData.setVelocityY(0.0);
        }

        playerData.setPlayerMoveSpeed(moveSpeed);
    }

    @EventHandler
    public void onViolation(PlayerViolationEvent event) {
        val player = event.getPlayer();

        val message = event.getAlertMessage();
        this.plugin.getAlertManager().alert(player, "", message);
    }
}
