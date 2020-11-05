package com.nicko.anticheat.check.impl.killaura;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraC extends PacketCheck {
    public KillAuraC(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Kill Aura§7[§cC§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInUseEntity &&
                ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK &&
                (player.isBlocking() || playerData.getActionManager().isPlacing()) &&
                this.onViolation(player, "flaggou " + name)) {
            //this.ban(player, name, 2);
        }
    }
}
