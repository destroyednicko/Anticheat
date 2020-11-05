package com.nicko.anticheat.check.checks;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;
import lombok.val;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class PostCheck extends PacketCheck {
    private final Class<? extends Packet> targetPacket;
    private long lastPostAction;
    private double vl;

    public PostCheck(AnticheatPlugin plugin, PlayerData playerData, Class<? extends Packet> targetPacket, String name) {
        super(plugin, playerData, name);

        this.targetPacket = targetPacket;
    }

    @Override
    public void run(Player player, Packet packet) {

    }

    public boolean isPost(Packet packet) {
        if (packet.getClass() == this.targetPacket) {
            val lastLocation = this.playerData.getLastLocation();

            if (lastLocation == null) {
                return false;
            }

            val now = System.currentTimeMillis();

            if (lastLocation.getTimestamp() + 20L > now) {
                this.lastPostAction = now;
            } else if (vl > 0) {
                vl -= 0.5;
            }
        } else if (packet instanceof PacketPlayInFlying) {
            val now = System.currentTimeMillis();

            if (this.lastPostAction + 100L > now) {
                return ++vl > 5;
            } else if (vl > 0) {
                vl -= 0.5;
            }
        }

        return false;
    }
}
