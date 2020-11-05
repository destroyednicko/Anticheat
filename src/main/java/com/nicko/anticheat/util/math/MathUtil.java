package com.nicko.anticheat.util.math;

import com.nicko.anticheat.util.nms.NmsUtil;
import lombok.val;
import lombok.var;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class MathUtil {
    public static final double HITBOX_NORMAL = 0.4;
    public static final double EXPANDER = Math.pow(2, 24);
    public static final double MIN_EXPANSION_VALUE = 0b100000000000000000;
    public static final double MIN_EUCILDEAN_VALUE = 0b100000000000000;
    public static final double HITBOX_DIAGONAL = Math.sqrt(Math.pow(HITBOX_NORMAL, 2) + Math.pow(HITBOX_NORMAL, 2));

    public static int timeToTicks(Player player) {
        return (int) Math.round(NmsUtil.getNmsPlayer(player).ping / 2L / 50.0) + 2;
    }

    public static double getHitboxSize(float yaw) {
        var clamped = (int) Math.abs(MathUtil.clamp180(yaw)) % 90;

        if (clamped > 45) {
            clamped = 90 - clamped;
        }

        clamped /= 0.45;

        val opposite = 100 - clamped;

        val diagonal = HITBOX_DIAGONAL * (clamped / 100.0);
        val normal = HITBOX_NORMAL * (opposite / 100.0);

        return diagonal + normal;
    }

    public static float getDistanceBetweenAngles(float from, float to) {
        float distance = Math.abs(from - to) % 360.0f;
        return distance > 180.f ? 360.f - distance : distance;
    }

    public static long getGcd(long current, long previous) {
        return previous <= MathUtil.MIN_EUCILDEAN_VALUE ? current :
                MathUtil.getGcd(previous, MathUtil.getDelta(current, previous));
    }

    private static long getDelta(long alpha, long beta) {
        return alpha % beta;
    }

    public static double clamp180(double theta) {
        theta %= 360.0;

        if (theta >= 180.0) {
            theta -= 360.0;
        }

        if (theta < -180.0) {
            theta += 360.0;
        }
        return theta;
    }

    public static float[] getRotationFromPosition(Location location, Location secondLocation) {
        double deltaX = location.getX() - secondLocation.getX(),
                deltaY = location.getY() - (secondLocation.getY() + 0.4),
                deltaZ = location.getZ() - secondLocation.getZ();

        double distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float yaw = (float)(Math.atan2(deltaZ, deltaX) * 180.0 / 3.141592653589793D) - 90.0F,
                pitch = (float)-(Math.atan2(deltaY, distance) * 180.0 / 3.141592653589793D);
        return new float[] { yaw, pitch };
    }

    public static double invSqrt(double x) {
        double xhalf = 0.5d * x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i >> 1);
        x = Double.longBitsToDouble(i);
        x *= (1.5d - xhalf * x * x);
        return x;
    }

    public static float wrapAngleTo180_float(float value)
    {
        value = value % 360.0F;

        if (value >= 180.0F)
        {
            value -= 360.0F;
        }

        if (value < -180.0F)
        {
            value += 360.0F;
        }

        return value;
    }

    public static double getAngleDistance(float angle1, float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0F;
        if (distance > 180.0F) {
            distance = 360.0F - distance;
        }
        return distance;
    }


    public static boolean isUsingOptifine(Location current, Location previous) {
        val yawChange = Math.abs(current.getYaw() - previous.getYaw());
        val pitchChange = Math.abs(current.getPitch() - previous.getPitch());

        val yawWrapped = MathUtil.wrapAngleTo180_float(yawChange);
        val pitchWrapped = MathUtil.wrapAngleTo180_float(pitchChange);

        return pitchWrapped < 0.01;
    }

    public static final Random RANDOM = new Random();

}
