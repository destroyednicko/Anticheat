package com.nicko.anticheat.check.impl.badpackets;

import com.nicko.anticheat.check.checks.PacketCheck;
import com.nicko.anticheat.util.math.MathUtil;
import com.nicko.anticheat.util.nms.NmsUtil;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;

public class BadPacketsG extends PacketCheck {

    public BadPacketsG(AnticheatPlugin plugin, PlayerData playerData) {
        super(plugin, playerData, "Bad Packets§7[§cG§7]");
    }

    private boolean sentFlyingPacket;
    private long lastFlying;

    @Override
    public void run(Player player, Packet packet) {
        if (packet instanceof PacketPlayInTransaction) {
            sentFlyingPacket = false;

            long delay = System.currentTimeMillis() - this.lastFlying;

            int ping = NmsUtil.getNmsPlayer(player).ping;

            if (delay < 20L) {
                return;
            }

            if (ping > (delay * 50L) * 2.75) {
                onViolation(player, "flaggou " + name);
                Bukkit.broadcastMessage(ping + " : " + String.valueOf((delay * 50L) * 2.75));
            }
        } else if (packet instanceof PacketPlayInFlying) {
            lastFlying = System.currentTimeMillis();

            if (sentFlyingPacket) {
                return;
            }
            NmsUtil.getNmsPlayer(player).playerConnection.sendPacket(new PacketPlayOutTransaction(0, (short) MathUtil.RANDOM.nextInt(1000), false));
            sentFlyingPacket = true;
        }
    }
}
