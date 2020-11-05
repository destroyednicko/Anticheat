package com.nicko.anticheat.check.impl.autoclicker;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;
import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AutoClickerB extends PacketCheck {
    private final List<Long> recentDelays = new ArrayList<>();

    public AutoClickerB(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Auto Clicker§7[§cB§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            val lastLocation = playerData.getLastLocation();

            if (lastLocation == null || this.playerData.getActionManager().isDigging()) {
                return;
            }

            val delay = System.currentTimeMillis() - lastLocation.getTimestamp();

            if (this.recentDelays.add(delay) && this.recentDelays.size() == 40) {
                val averageDelay = this.recentDelays.stream()
                        .mapToDouble(Long::doubleValue)
                        .average()
                        .orElse(0.0);

                if (averageDelay <= this.recentDelays.size() && this.onViolation(player, "flaggou " + name)) {
                    //this.ban(player, name, 5);
                }

                this.recentDelays.clear();
            }

        }
    }
}
