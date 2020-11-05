package com.nicko.anticheat.punishment;

import com.nicko.anticheat.util.BanHammer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;

@Getter
@RequiredArgsConstructor
public class Punishment {

    private final BanHammer banHammer;
    private final CommandSender sender;

    private final String playerName;
    private final String reason;

    @Setter
    private int delay = 0;
}
