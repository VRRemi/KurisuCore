package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.data.Tag;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NameManager {

    public NameManager() {
        init();
    }

    private void init() {
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

    

}
