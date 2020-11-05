package com.nicko.anticheat.check.impl.noslow;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;

public class NoSlowA extends PacketCheck {
    public NoSlowA(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "No Slow§7[§cA§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInBlockDig &&
                ((PacketPlayInBlockDig) packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM &&
                this.playerData.getActionManager().isPlacing() && this.onViolation(player, "flaggou " + name)) {
            //this.ban(player, name, 2);
        }
    }
}
