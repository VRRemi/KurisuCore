package dev.vrremi.kurisucore.utils;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Punishment;
import dev.vrremi.kurisucore.data.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigUtils {

    public static void sendMessage(CommandSender sender, String path) {
        sender.sendMessage(CC.color(KurisuCore.getConfigManager().getMessage(path)));
    }

    public static void sendMessage(CommandSender sender, String path, HashMap<String, String> placeholders) {
        AtomicReference<String> message = new AtomicReference<>(FubukiCore.getConfigManager().getMessage(path));
        placeholders.forEach((key, value) -> {
            message.set(message.get().replace(key, value));
        });
        sender.sendMessage(CC.color(message.get()));
    }

    public static void broadcast(String path, String permission) {
        if (permission == null || permission.isEmpty())
            Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, path));
        else
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission)).forEach(player -> sendMessage(player,
                    path));
    }

    public static void broadcast(String path, String permission, HashMap<String, String> placeholders) {
        if (permission == null || permission.isEmpty())
            Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, path, placeholders));
        else
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission)).forEach(player -> sendMessage(player, path,
                    placeholders));
    }

    public static void staffChat(String path, String permission, HashMap<String, String> Placeholders) {
        if (permission == null || permission.isEmpty())
            Bukkit.getOnlinePlayers().forEach(player -> {
                User user = KurisuCore.getUserManager().getUser(player);
                if (user != null && !user.isStaffChatHidden())
                    sendMessage(player, path, placeholders);
            });
        else
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission)).forEach(player -> {
                User user = KurisuCore.getUserManager().getUser(player);
                if (user != null && !user.isStaffChatHidden())
                    sendMessage(player, path, placeholders);
            });
    }

    public static String getBanMessage(Punishment punishment) {
        return getPunishmentMessage(punishment, "ban-format");
    }

    public static String getMuteMessage(Punishment punishment) {
        return getPunishmentMessage(punishment, "mute-format");
    }

    public static String getKickMessage(Punishment punishment) {
        return getPunishmentMessage(punishment, "kick-format");
    }

    public static String getWarnMessage(Punishment punishment) {
        return getPunishmentMessage(punishment, "warn-format");
    }

    private static String getPunishmentMessage(Punishment punishment, String path) {
        List<String> lines = KurisuCore.getConfigManager().getMultiMessage(path);
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (String line : lines) {
            String expire = punishment.getTimeout() == Long.MAX_VALUE ? "Never" :
                    Time.millisToTime(punishment.getTimeout());
            sb.append(CC.color(
                    line
                            .replace("{expire}", expire)
                            .replace("{reason}", punishment.getReason().isEmpty() ? "No reason provided" :
                                    punishment.getReason())
                            .replace("{punisher}", punishment.getPunisher())
                            .replace("{id}", punishment.getId())
            ));
            if (index < lines.size()) {
                sb.append("\n");
            }
            index++;
        }
        return sb.substring(0, sb.length() - 1);
    }
}
