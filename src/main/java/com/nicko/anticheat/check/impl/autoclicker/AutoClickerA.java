package com.nicko.anticheat.check.impl.autoclicker;

import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;

public class AutoClickerA extends PacketCheck {
    private long lastSwing, lastDelay;
    private int vl;

    public AutoClickerA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Auto Clicker§7[§cA§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            val now = System.currentTimeMillis();

            if (this.playerData.getActionManager().isDigging()) {
                return;
            }

            val delay = now - this.lastSwing;

            if (delay > 10L && delay < 200L) {
                if (Math.abs(delay - this.lastDelay) > 50L || this.playerData.getActionManager().isDigging()) {
                    this.vl = 0;

                } else if (delay > 35L && ++this.vl > 80) {
                    this.onViolation(player, "flaggou " + name);

                    //this.ban(player, name, 100);
                }

                this.lastDelay = delay;
            }

            this.lastSwing = now;
        }
    }
}
