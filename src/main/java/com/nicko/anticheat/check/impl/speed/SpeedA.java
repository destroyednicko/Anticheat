package com.nicko.anticheat.check.impl.speed;

import lombok.val;
import lombok.var;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.PositionUpdate;

public class SpeedA extends PositionCheck {

    private double lastSpeed;
    public SpeedA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Speed§7[§cA§7]");
    }

    private int threshold;

    @Override
    public void run(Player player, PositionUpdate update) {
        if (player.getAllowFlight() || player.isInsideVehicle()) {
            return;
        }

        val nmsPlayer = ((CraftPlayer) player).getHandle();

        val to = update.getTo();
        val from = update.getFrom();

        val dx = to.getX() - from.getX();
        val dy = to.getY() - from.getY();
        val dz = to.getZ() - from.getZ();

        /**
         * @See net.minecraft.server.v1_8_R3.EntityLiving#L1238
         */
        var f5 = 0.91f;

        val onGround = nmsPlayer.onGround;

        @SuppressWarnings("UnusedAssignment")
        var moveSpeed = 0.0f;

        if (onGround) {
            f5 = nmsPlayer.world.getType(new BlockPosition(MathHelper.floor(to.getX()),
                    MathHelper.floor(nmsPlayer.getBoundingBox().b) - 1,
                    MathHelper.floor(to.getZ())))
                    .getBlock()
                    .frictionFactor * 0.91F;
        }

        val f6 = 0.16277136F / (f5 * f5 * f5);

        if (onGround) {
            moveSpeed = nmsPlayer.bI() * f6;

            if (playerData.isSprinting() && moveSpeed < 0.129) {
                moveSpeed *= 1.3;
            }

            if (dy > 0.0001) {
                moveSpeed += 0.2;
            }
        } else {
            moveSpeed = nmsPlayer.aM + 0.00001f;

            if (this.playerData.isSprinting() && moveSpeed < 0.026) {
                moveSpeed += 0.006;
            }

            if (dy < -0.08 && player.getFallDistance() == 0.0) {
                moveSpeed *= Math.abs(dy) * 1.3;
            }
        }

        val previousSpeed = this.lastSpeed;
        val speed = Math.sqrt(dx * dx + dz * dz);

        val speedChange = speed - previousSpeed;

        moveSpeed += Math.sqrt(this.playerData.getVelocityManager().getMaxHorizontal());

        if (speed > 0.24 && speedChange - moveSpeed > 0.001) {
            if ((threshold += 10) > 15) {
                this.onViolation(player, "flaggou " + super.name);
            }
        } else {
            threshold = Math.max(threshold - 1, 0);
        }

        //@See net.minecraft.server.v1_8_R3.EntityLiving#L1285
        this.lastSpeed = speed * f5;
    }
}
