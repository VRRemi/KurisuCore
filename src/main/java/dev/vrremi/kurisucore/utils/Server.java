package dev.vrremi.kurisucore.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Server {

    public static Player getOnline(String name) {
        return Bukkit.getOnlinePlayers().stream().filter(online -> online.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
