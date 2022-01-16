package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.events.PlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class EventManager {

    public EventManager() {
        registerEvents();
    }

    public void registerEvents() {
        registerEvents(new PlayerEvent());
    }

    public void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, KurisuCore.getInstance());
    }
}
