package com.nicko.anticheat;

import com.nicko.anticheat.alert.AlertManager;
import com.nicko.anticheat.client.ClientManager;
import com.nicko.anticheat.data.PlayerData;
import com.nicko.anticheat.handler.MovementHandler;
import com.nicko.anticheat.listener.PlayerListener;
import com.nicko.anticheat.punishment.Punishment;
import com.nicko.anticheat.punishment.PunishmentManager;
import com.nicko.anticheat.util.BanHammer;
import com.nicko.anticheat.util.date.BanDate;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AnticheatPlugin extends JavaPlugin {

    // Esse map armazena o PlayerData para cada jogador
    private final Map<UUID, PlayerData> playerData = new ConcurrentHashMap<>();


    // Esse é o 'banHammer', que contém os argumentos.
    private final BanHammer banHammer = new BanHammer(new BanDate(90, 0, 0), "/ban");

    @Getter
    private AlertManager alertManager;

    @Getter
    private PunishmentManager punishmentManager;

    @Getter
    private ClientManager clientManager;

    @Getter
    private MovementHandler movementHandler;

    @Getter
    private final Executor packetThread = Executors.newSingleThreadExecutor();

    @Getter
    private int currentTick;
    @Getter
    private int passedTicks;

    public void onEnable() {
        this.punishmentManager = new PunishmentManager(this);
        this.alertManager = new AlertManager(this);
        this.movementHandler = new MovementHandler(this);
        this.clientManager = new ClientManager(this);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                currentTick = currentTick >= 20 ? 1 : currentTick + 1;
                passedTicks++;
            }
        }.runTaskTimer(this, 1, 1);
    }

    /**
     * Retorna o 'playerData' do {@param player}
     *
     * @param player - o jogador para obter os dados
     * @return - o 'playerData' do {@param player}
     */
    public PlayerData getData(Player player) {
        return this.playerData.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData(this));
    }

    /**
     * Realiza a punição por anticheat do {@param player}
     *
     * @param player - O jogador que será punido
     * @param playerData - O 'playerData' do jogador
     * @param reason - O motivo pelo qual o jogador foi banido
     */
    public void doBan(Player player, PlayerData playerData, String reason) {
        this.alertManager.alert(String.format("Punindo %s por %s", player.getName(), reason));

        this.punishmentManager.executePunishment(new Punishment(this.banHammer,
                this.getServer().getConsoleSender(),
                player.getName(),
                "Cheating"));
    }

}