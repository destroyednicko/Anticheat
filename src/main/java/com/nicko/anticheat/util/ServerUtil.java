package com.nicko.anticheat.util;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class ServerUtil {

    private int getTps() {
        // não adianta retornar com double, porque a verificação só iria ocorrer se o tps estivesse menos de 19
        return (int) MinecraftServer.getServer().recentTps[0];
    }
}
