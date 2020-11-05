package com.nicko.anticheat.check.impl.killaura;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraD extends PacketCheck {
    private int swings;
    private int attacks;

    public KillAuraD(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Kill Aura§7[§cD§7]");
    }

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            ++this.swings;
        } else if (packet instanceof PacketPlayInFlying) {
            if ((this.swings < this.attacks || (this.attacks != 0 && this.swings > this.attacks))) {
                this.onViolation(player, "flaggou " + name);
            }

            this.swings = 0;
            this.attacks = 0;
        } else if (packet instanceof PacketPlayInUseEntity) {
            if (((PacketPlayInUseEntity) packet).a() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                return;
            }

            ++this.attacks;
        }
    }
}
