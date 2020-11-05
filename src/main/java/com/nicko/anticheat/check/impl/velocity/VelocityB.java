package com.nicko.anticheat.check.impl.velocity;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PositionCheck;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.update.impl.PositionUpdate;
import lombok.val;
import org.bukkit.entity.Player;

public class VelocityB extends PositionCheck {
    private int threshold;

    public VelocityB(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Velocity§7[§cA§7]");
    }

    @Override
    public void run(Player player, PositionUpdate update) {
        val dy = update.getTo().getY() - update.getFrom().getY();

        if (this.playerData.getVelocityY() > 0.0 && this.playerData.isWasOnGround() &&
                !this.playerData.isBelowBlock() && !this.playerData.isWasBelowBlock() &&
                !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() &&
                !this.playerData.isWasInWeb() && dy > 0.0 && dy < 0.41999998688697815 && update.getFrom().getY() % 1.0 == 0.0) {

            val quotient = dy / this.playerData.getVelocityY();

            // o quociente do delta y e y do velocity dos jogadores (enviado do servidor) menor que 0.41999998688697815 (tamanho máximo do pulo)
            // o que significa que eles estão usando um cheating de velocity vertical
            if (quotient < 0.99 && (threshold += 30) > 35 && this.onViolation(player, "flaggou " + name)) {
               // this.ban(player, name, 12);
            }
        }

        if (threshold > 0) {
            --threshold;
        }
    }
}
