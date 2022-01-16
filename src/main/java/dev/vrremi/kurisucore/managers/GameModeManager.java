package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.utils.Serialize;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GameModeManager {

    public GameModeManager {
        init();
    }

    private void init() {
        createBase();
    }

    public void setGameMode(Player player, GameMode gameMode) {
        create(player);
        save(player);
        player.setGameMode(gameMode);
        Serialize.load(player, get(player));
    }

    

}
