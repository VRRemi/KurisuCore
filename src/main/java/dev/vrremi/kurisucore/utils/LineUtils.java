package dev.vrremi.kurisucore.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LineUtils {

    public static void addHeader(List<String> lines, CommandSender sender) {
        addHeader(lines, sender, "&7");
    }

    public static void addHeader(List<String> lines, CommandSender sender, String color) {
        if (sender instanceof Player) {
            lines.add(CC.color(color + "&m" + StringUtils.repeat(" ", 50) + "&r"));
        } else {
            lines.add(CC.color(color + StringUtils.repeat("=", 50)));
        }
    }
}
