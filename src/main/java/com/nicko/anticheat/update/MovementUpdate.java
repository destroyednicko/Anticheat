package com.nicko.anticheat.update;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class MovementUpdate {
    // Isso torna mais fácil do que armazenar os valores de yaw e pitch na classe PlayerData

    // Precisa fazer 2 atualizações diferentes pra posição e rotação pra não se misturarem
    private final Player player;
    private final Location to, from;
}
