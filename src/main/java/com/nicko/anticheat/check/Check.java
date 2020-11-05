package com.nicko.anticheat.check;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.event.PlayerViolationEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class Check<T> {
    private final AnticheatPlugin plugin;
    public final PlayerData playerData;

    @Getter
    private final Class<T> clazz;

    @Getter
    public final String name;

    private final List<Long> alerts = new ArrayList<>();

    public boolean onViolation(Player player, String message) {
        val now = System.currentTimeMillis();

        // pra não armazenar duas vezes...
        if (this.alerts.contains(now)) {
            return false;
        }

        this.alerts.add(now);

        message = message.replace("flaggou", "§7flaggou§c");

        val event = new PlayerViolationEvent(player, message + String.format(" §7[§cVL%s§7]", this.getVl()), false);

        this.plugin.getServer().getPluginManager().callEvent(event);

        return !event.isCancelled();
    }

    public void ban(Player player, String message, int vl) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            if (this.getVl() > vl) {
                player.kickPlayer(message);
            }

        });
    }

    /**
     * Retorna a quantidade de flags
     *
     * @return - o check flaggado
     */
    private int getVl() {
        return Math.toIntExact(this.alerts.stream()
                .filter(timestamp -> timestamp + 60000L > System.currentTimeMillis())
                .count());
    }

    public abstract void run(Player player, T p);
}
