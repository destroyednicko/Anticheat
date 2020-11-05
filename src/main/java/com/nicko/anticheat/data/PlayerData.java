package com.nicko.anticheat.data;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.action.ActionManager;
import com.nicko.anticheat.check.Check;
import com.nicko.anticheat.check.impl.aimassist.AimAssistA;
import com.nicko.anticheat.check.impl.aimassist.AimAssistB;
import com.nicko.anticheat.check.impl.aimassist.AimAssistC;
import com.nicko.anticheat.check.impl.aimassist.AimAssistD;
import com.nicko.anticheat.check.impl.autoclicker.AutoClickerA;
import com.nicko.anticheat.check.impl.autoclicker.AutoClickerB;
import com.nicko.anticheat.check.impl.autoclicker.AutoClickerC;
import com.nicko.anticheat.check.impl.autoclicker.AutoClickerD;
import com.nicko.anticheat.check.impl.badpackets.*;
import com.nicko.anticheat.check.impl.fly.FlyA;
import com.nicko.anticheat.check.impl.fly.FlyB;
import com.nicko.anticheat.check.impl.invalidmove.InvalidMoveA;
import com.nicko.anticheat.check.impl.jesus.JesusA;
import com.nicko.anticheat.check.impl.killaura.*;
import com.nicko.anticheat.check.impl.noslow.NoSlowA;
import com.nicko.anticheat.check.impl.reach.ReachA;
import com.nicko.anticheat.check.impl.speed.SpeedA;
import com.nicko.anticheat.check.impl.timer.TimerA;
import com.nicko.anticheat.check.impl.velocity.VelocityA;
import com.nicko.anticheat.check.impl.velocity.VelocityB;
import com.nicko.anticheat.check.impl.velocity.VelocityC;
import com.nicko.anticheat.location.PlayerLocation;
import com.nicko.anticheat.parser.MovementParser;
import com.nicko.anticheat.teleport.TeleportManager;
import com.nicko.anticheat.velocity.VelocityManager;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class PlayerData {

    // todos os checks
    private final Class[] checks = new Class[] {
            AimAssistA.class, AimAssistB.class, AimAssistC.class, AimAssistD.class,

            AutoClickerA.class, AutoClickerB.class, AutoClickerC.class, AutoClickerD.class,

            BadPacketsA.class, BadPacketsB.class, BadPacketsC.class, BadPacketsD.class, BadPacketsE.class, BadPacketsF.class, BadPacketsG.class,
            BadPacketsF.class,

            SpeedA.class, FlyA.class, FlyB.class,

            InvalidMoveA.class,

            JesusA.class,

            KillAuraA.class, KillAuraB.class, KillAuraC.class, KillAuraD.class, KillAuraE.class, KillAuraF.class,

            ReachA.class,

            NoSlowA.class,

            TimerA.class,

            VelocityA.class, VelocityB.class, VelocityC.class,

    };

    private final List<Check> checkList = new ArrayList<>();
    private final List<PlayerLocation> playerLocations = new ArrayList<>();

    private PlayerLocation lastLocation;

    // talvez colocar no "actionManager'
    private boolean sprinting;

    private long lastJoinTime, lastFlying, lastDelayedPacket;

    private double velocityX;
    private double velocityY;
    private double velocityZ;

    private double playerMoveSpeed;

    private final ActionManager actionManager = new ActionManager();
    private final MovementParser movementParser = new MovementParser();
    private final TeleportManager teleportManager = new TeleportManager();
    private final VelocityManager velocityManager = new VelocityManager();

    public PlayerData(AnticheatPlugin plugin) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> Arrays.stream(checks).forEach(clazz -> {
                    Check<?> check;

                    try {
                        // Isso é melhor do que enviar spam toda vez que for adicionar um novo check

                        check = (Check<?>) clazz
                                .getConstructor(AnticheatPlugin.class, PlayerData.class)
                                .newInstance(plugin, this);
                    } catch (InstantiationException | NoSuchMethodException |
                            InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();

                        throw new RuntimeException("Exception...");
                    }

                    this.checkList.add(check);
                }));
    }


    public <T> T getCheck(Class<T> clazz) {
        return (T) this.checkList.stream()
                .filter(check -> check.getClass() == clazz)
                .findFirst()
                .orElse(null);
    }

    public boolean isOnGround() {
        return this.movementParser.onGround;
    }

    public boolean isInLiquid() {
        return this.movementParser.inLiquid;
    }

    public boolean isOnLadder() {
        return this.movementParser.onLadder;
    }

    public boolean isBelowBlock() {
        return this.movementParser.belowBlock;
    }

    public boolean isWasOnGround() {
        return this.movementParser.wasOnGround;
    }

    public boolean isWasInLiquid() {
        return this.movementParser.wasInLiquid;
    }

    public boolean isWasOnLadder() {
        return this.movementParser.wasOnLadder;
    }

    public boolean isWasBelowBlock() {
        return this.movementParser.wasBelowBlock;
    }

    public boolean isInWeb() {
        return this.movementParser.inWeb;
    }

    public boolean isWasInWeb() {
        return this.movementParser.wasWeb;
    }
}
