package com.nicko.anticheat.punishment;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.punishment.task.PunishmentRunnable;

@RequiredArgsConstructor
public class PunishmentManager {

    private final AnticheatPlugin plugin;

    /**
     * Executa uma punição
     *
     * @param punishment A punição pra executar.
     */
    public void executePunishment(Punishment punishment) {
        Bukkit.getScheduler().runTaskLater(plugin, new PunishmentRunnable(plugin, punishment),
                punishment.getDelay() + 1L);
    }
}
