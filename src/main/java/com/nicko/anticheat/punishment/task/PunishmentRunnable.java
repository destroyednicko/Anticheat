package com.nicko.anticheat.punishment.task;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.punishment.Punishment;
import com.nicko.anticheat.util.CC;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PunishmentRunnable implements Runnable {

    private final AnticheatPlugin plugin;
    private final Punishment punishment;

    @Override
    public void run() {
        if (punishment.getReason().equals("Cheating")) {
            this.plugin.getServer()
                    .broadcastMessage(String.format("%sPurificador %sdetectou que %s%s %sestava fazendo utilização de programas ilegais.",
                    CC.RED,
                    CC.GOLD,
                    CC.RED,
                    punishment.getPlayerName(),
                    CC.GOLD));
        }

        this.plugin.getServer().dispatchCommand(punishment.getSender(), String.format("%s %s %s %dd",
                punishment.getBanHammer().getCommand(),
                punishment.getPlayerName(),
                punishment.getReason(),
                punishment.getBanHammer().getBanDate().getDays()));
    }
}
