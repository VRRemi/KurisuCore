package dev.vrremi.kurisucore.utils;

import dev.vrremi.kurisucore.KurisuCore;
import org.bukkit.Bukkit;

public class Threading {

    public static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(KurisuCore.getInstance(), runnable);
    }
}
