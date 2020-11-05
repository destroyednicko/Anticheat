package com.nicko.anticheat.check.impl.autoclicker;

import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;

public class AutoClickerC extends PacketCheck {
    private boolean dug;
    private int vl;

    public AutoClickerC(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Auto Clicker§7[§cC§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            val digType = ((PacketPlayInBlockDig) packet).c();

            if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                this.dug = true;
            } else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                if (this.dug) {
                    if (++vl > 9 && this.onViolation(player, "flaggou " + name)) {
                        //this.ban(player, name, 10);
                    }
                } else if (this.vl > 0) {
                    this.vl -= 3;
                }
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            this.dug = false;
        }
    }
}
