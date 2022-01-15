package dev.vrremi.kurisucore.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Serialize {

    public static String get(Player player) {
        String s = to(player.getInventory().getStorageContents());
        String a = to(player.getInventory().getArmorContents());
        String e = to(player.getInventory().getExtraContents());
        return (s + "_" + a + "_" + e).replace("\r\\", "").replace("\r", "").replace("\n", "");
    }

    public static void load(Player player, String data) {
        player.getInventory().setStorageContents(null);
        player.getInventory().setExtraContents(null);
        player.getInventory().setArmorContents(null);
        if (data == null || data.isEmpty()) return;
        try {
            player.getInventory().setStorageContents(from(data.split("_")[0]));
            player.getInventory().setArmorContents(from(data.split("_")[1]));
            player.getInventory().setExtraContents(from(data.split("_")[2]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String to(ItemStack[] i) throws IllegalStateException {
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            BukkitObjectOutputStream d = new BukkitObjectOutputStream(o);
            d.writeInt(i.length);
            for (ItemStack t : i.clone()) {
                if (t == null) {
                    t = new ItemStack(Material.AIR);
                }
                d.writeObject(t);
            }
            d.close();
            return Base64Coder.encodeLines(o.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
}
