package com.nicko.anticheat.action;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ActionManager {

    @Setter
    private boolean digging;

    private boolean placing;
    private boolean attacking;
    private boolean swinging;

    private long lastAttack;

    public void onArmAnimation() {
        this.swinging = true;
    }

    public void onAttack() {
        this.attacking = true;

        this.lastAttack = System.currentTimeMillis();
    }

    public void onPlace() {
        this.placing = true;
    }

    public void onFlying() {
        this.placing = false;
        this.attacking = false;
        this.swinging = false;
    }


}
