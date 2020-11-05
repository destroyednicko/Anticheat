package com.nicko.anticheat.check.impl.autoclicker;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;

public class AutoClickerD extends PacketCheck {
    private int threshold;
    private int clickDelay;

    public AutoClickerD(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Auto Clicker§7[§cD§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.playerData.getActionManager().isDigging()) {
                return;
            }

            if (clickDelay++ < 3) {
                if (++threshold > 50) {
                    this.onViolation(player, "flaggou (E) " + name);
                }
            } else {
                threshold /= 4;
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            clickDelay = 0;
            threshold--;
        }
    }
}
