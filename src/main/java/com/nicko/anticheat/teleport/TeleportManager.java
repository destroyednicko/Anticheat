package com.nicko.anticheat.teleport;

import com.nicko.anticheat.location.PlayerLocation;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class TeleportManager {

    private final List<Teleport> locations = new ArrayList<>();

    private final Predicate<Teleport> shouldRemoveTeleport = teleport -> {
        val now = System.currentTimeMillis();

        return teleport.getCreationTime() + 2000L < now;
    };

    /**
     * Adiciona a entry de teleporte à lista
     *
     * @param x - X do teleporte
     * @param y - Y do teleporte
     * @param z - Z do teleporte
     */
    public void add(double x, double y, double z) {
        this.removeOldEntries();

        this.locations.add(new Teleport(x, y, z));
    }

    /**
     * Retorna se o jogador foi teleporta em {@param delta} metros quadrados da distancia da {@param location}
     *
     * @param location - o local pra verificar se o o jogador se teleportou
     * @param delta - A distância mínima do delta
     * @return - true ou false, dependendo do argumento especificado
     */
    public boolean hasTeleported(PlayerLocation location, double delta) {
        this.removeOldEntries();

        val isClose = new AtomicBoolean(false);

        // se não tiver nenhuma entry de teleporte, retorna false
        if (this.locations.isEmpty()) {
            isClose.set(false);
        }

        this.locations.forEach(teleport -> {
            val dx = location.getX() - teleport.getX();
            val dy = location.getY() - teleport.getY();
            val dz = location.getZ() - teleport.getZ();

            // A distância quadrada é menor que a do delta
            if (Math.pow(dx, 2) + Math.pow(dy, 2) * Math.pow(dz, 2) < Math.pow(delta, 2)) {
                isClose.set(true);
            }
        });

        return isClose.get();
    }

    /**
     * Remove as entrys de teleporte antigas que expiram em 2 segundos
     */
    public void removeOldEntries() {
        this.locations.removeIf(shouldRemoveTeleport);
    }

}
