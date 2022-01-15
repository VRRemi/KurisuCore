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
    
}