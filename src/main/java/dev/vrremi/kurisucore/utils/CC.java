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

    public static ArrayList<Color> colorsBetween(Color color1, Color color2, int steps) {
        steps--;
        ArrayList<Color> colorList = new ArrayList<>();
        double redInc = Math.abs((double) color1.getRed() - color2.getRed()) / steps;
        double greenInc = Math.abs((double) color1.getGreen() - color2.getGreen()) / steps;
        double blueInc = Math.abs((double) color1.getBlue() - color2.getBlue()) / steps;
        for (int i = 0; i < steps; i++) {
            colorList.add(new Color(
                    (int) (color1.getRed() > color2.getRed() ? color1.getRed() - redInc * i : color1.getRed() + redInc * i),
                    (int) (color1.getGreen() > color2.getGreen() ? color1.getGreen() - greenInc * i : color1.getGreen() + greenInc * i),
                    (int) (color1.getBlue() > color2.getBlue() ? color1.getBlue() - blueInc * i : color1.getBlue() + blueInc * i)
            ));
        }
        colorList.add(color2);
        return colorList;
    }

    public static Material getMaterialFromColor(String color) {
        if (color.isEmpty()) return Material.WHITE_WOOL;
        color = color
                .replace("&", "")
                .replace("l", "")
                .replace("m", "")
                .replace("n", "")
                .replace("o", "")
                .replace("k", "")
                .replace("r", "")
                .toLowerCase();
        if (color.isEmpty()) return Material.WHITE_WOOL;
        switch (String.valueOf(color.charAt(0))) {
            case "0":
                return Material.BLACK_WOOL;
            case "1":
                return Material.BLUE_WOOL;
            case "2":
                return Material.GREEN_WOOL;
            case "3":
                return Material.CYAN_WOOL;
            case "4":
                return Material.RED_WOOL;
            case "5":
                return Material.PURPLE_WOOL;
            case "6":
                return Material.ORANGE_WOOL;
            case "7":
                return Material.LIGHT_GRAY_WOOL;
            case "8":
                return Material.GRAY_WOOL;
            case "9":
            case "a":
                return Material.LIME_WOOL;
            case "b":
                return Material.LIGHT_BLUE_WOOL;
            case "c":
                return Material.PINK_WOOL;
            case "d":
                return Material.MAGENTA_WOOL;
            case "e":
                return Material.YELLOW_WOOL;
        }
        return Material.WHITE_WOOL;
    }

}