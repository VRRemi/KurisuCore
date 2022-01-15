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

    
}
