package com.nicko.anticheat.check.impl.badpackets;

import com.nicko.anticheat.check.checks.PacketCheck;
import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;

public class BadPacketsD extends PacketCheck {
    private boolean sentAction;

    public BadPacketsD(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Bad Packets§7[§cD§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInEntityAction) {
            val playerAction = ((PacketPlayInEntityAction)packet).b();

            if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING ||
                    playerAction == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                if (this.sentAction) {
                    if (this.onViolation(player, "flaggou " + name)) {
                        //this.ban(player, name, 2);
                    }
                } else {
                    this.sentAction = true;
                }
            }
        } else if (packet instanceof PacketPlayInFlying) {
            this.sentAction = false;
        }
    }
}
