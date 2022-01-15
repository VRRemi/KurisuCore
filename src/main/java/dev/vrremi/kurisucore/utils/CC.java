package dev.vrremi.kurisucore.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CC {

    private static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> lore) {
        List<String> newLore = new ArrayList<>();
        lore.forEach(line -> newLore.add(CC.color(line)));
        return newLore;
    }
    
    public static String formatPlaceholders(String string, String light, String dark) {
        return string
                .replace("/", dark + "/" + light)
                .replace("<", dark + "<" + light)
                .replace(">", dark + ">" + light)
                .replace("[", dark + "[" + light)
                .replace("]", dark + "]" + light)
                .replace("-", dark + "-" + light);
    }
}