package com.nicko.anticheat.client;

import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ClientManager {
    private final AnticheatPlugin plugin;

    private final Set<ClientType> clients = new HashSet<>();

    public ClientManager(AnticheatPlugin plugin) {
        this.plugin = plugin;

        this.clients.add(new ClientType("cock"));
        this.clients.add(new ClientType("lmaohax"));
        this.clients.add(new ClientType("LOLIMAHCKER"));
        this.clients.add(new ClientType("L0LIMAHCKER"));
        this.clients.add(new ClientType("MCnetHandler"));
        this.clients.add(new ClientType("0SO1Lk2KASxzsd"));
        this.clients.add(new ClientType("mincraftpvphcker"));
        this.clients.add(new ClientType("customGuiOpenBspkrs"));
        this.clients.add(new ClientType("CPS_BAN_THIS_NIGGER"));
    }

    public boolean isCheating(Player player, PlayerData playerData, String payload) {
        return this.clients.stream().anyMatch(clientType -> clientType.getPayload().equals(payload));
    }
}
