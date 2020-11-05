package com.nicko.anticheat.velocity;

import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Predicate;

public class VelocityManager {

    @Getter
    private final List<Velocity> velocities = new ArrayList<>();

    private final Predicate<Velocity> shouldRemoveVelocity = velocity -> {
        val now = System.currentTimeMillis();

        return velocity.getCreationTime() + 2000L < now;
    };

    /**
     * Adiciona uma 'entry' de velocity à lista
     *
     * @param x - A distancia X do velocity
     * @param y  - A distancia absoluta Y do velocity
     * @param z - A distancia Z do velocity
     */
    public void addVelocityEntry(double x, double y, double z) {
        this.velocities.add(new Velocity(x * x + z * z, Math.abs(y)));
    }

    /**
     * Obtém a distância horizontal máxima do velocity num quadrado
     *
     * @return - A maior distância de 'velocity' horizontal dentro de 2 seconds (40 ticks)
     */
    public double getMaxHorizontal() {
        this.velocities.removeIf(shouldRemoveVelocity);

        try {
            return Math.sqrt(this.velocities.stream()
                    .mapToDouble(Velocity::getHorizontal)
                    .max()
                    .orElse(0.f));
        } catch (ConcurrentModificationException e) {
            return 1.0;
        }
    }

    /**
     * Obtem a distancia vertical máxima e absoluta do velocity
     *
     * @return - A maior distancia vertical máxima e absoluta do velocity dentro de 2 seconds (40 ticks)
     */
    public double getMaxVertical() {
        this.velocities.removeIf(shouldRemoveVelocity);

        return this.velocities.stream()
                .mapToDouble(Velocity::getVertical)
                .max()
                .orElse(0.f);
    }


}
