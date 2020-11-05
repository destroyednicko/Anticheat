package com.nicko.anticheat.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PlayerLocation {
    // esta classe é usada quando armazenamos a loc. de cada jogador por 20 ticks (1 segundo)

    private double x, y, z;
    private float yaw, pitch;
    private final  long timestamp = System.currentTimeMillis();
}
