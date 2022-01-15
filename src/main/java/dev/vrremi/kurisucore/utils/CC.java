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

    public static String format(int num) {
        return NumberFormat.getInstance().format(num);
    }

    public static String format(double num) {
        return NumberFormat.getInstance().format(num);
    }

    public static String formatHex(String string) {
        Matcher match = hexPattern.matcher(string);
        while (match.find()) {
            String color = string.substring(match.start(), match.end());
            string = string.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            match = hexPattern.matcher(string);
        }
        return string;
    }

    public static String getCustomAuthorName() {
        String name = "vrremi";
        List<String> colors = new ArrayList<>();
        for (Color color : colorsBetween(new Color(0x01acff), new Color(0xffffff), name.length())) {
            colors.add("#" + String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            stringBuilder.append(colors.get(i)).append(name.charAt(i));
        }
        return formatHex(stringBuilder.toString());
    }
    
}