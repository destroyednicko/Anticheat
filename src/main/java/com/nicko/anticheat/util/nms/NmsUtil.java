package com.nicko.anticheat.util.nms;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NmsUtil {

    /**
     * Retorna o 'entityPlayer' do {@param player} declarado
     *
     * @param player - o jogador para retornar o 'EntityPlayer'
     * @return - o 'EntityPlayer' instanciado de {@param player}
     */
    public static EntityPlayer getNmsPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
