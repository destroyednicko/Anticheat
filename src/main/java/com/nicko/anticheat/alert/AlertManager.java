package com.nicko.anticheat.alert;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import com.nicko.anticheat.AnticheatPlugin;
import com.nicko.anticheat.util.CC;
import com.nicko.anticheat.util.message.mkremins.fanciful.FancyMessage;

import java.util.Arrays;

@RequiredArgsConstructor
public class AlertManager {
    private final AnticheatPlugin plugin;

    public void alert(Player target, String permission, Object... args) {
        StringBuilder sb = new StringBuilder("§a§lPurificador §7┃ ");

        sb.append(CC.WHITE);
        sb.append(target.getDisplayName());
        sb.append(" ");
        /*val message = new FancyMessage("[AC] ")
                .color(CC.DARK_PURPLE);

        message.append(target.getDisplayName() + " ")
                .color(CC.PINK)
                .tooltip(String.format("%sTeleportar para %s", CC.WHITE, target.getDisplayName()))
                .command(String.format("/tp %s", target.getName()));*/

        Arrays.stream(args)
                .filter(String.class::isInstance)
                .forEach(arg -> {
                    sb.append(CC.GRAY);
                    sb.append(arg);
                    sb.append(" ");
                });

        plugin.getServer().getOnlinePlayers().stream()
              //  .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(sb.toString()));
    }

    public void alert(String permission, Object... args) {
        val message = new FancyMessage("Purificador ┃ ")
                .color(CC.RED);

        Arrays.stream(args)
                .filter(String.class::isInstance)
                .forEach(arg -> message
                        .append(arg)
                        .color(CC.GOLD));

        plugin.getServer().getOnlinePlayers().stream()
               // .filter(player -> player.hasPermission(permission))
                .forEach(message::send);
    }
}
