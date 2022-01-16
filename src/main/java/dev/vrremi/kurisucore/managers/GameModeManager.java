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

    private void create(Player player) {
        File file = getFile(player);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileConfiguration config = getConfigFile(player);
                Arrays.stream(GameMode.values()).filter(gameMode -> !gameMode.equals(GameMode.SPECTATOR)).forEach(gameMode -> config.set("inventory." + gameMode, ""));
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String get(Player player) {
        File file = getFile(player);
        if (file.exists()) {
            FileConfiguration config = getConfigFile(player);
            return config.getString("inventory." + player.getGameMode());
        }
        return null;
    }

    private void save(Player player) {
        File file = getFile(player);
        if (file.exists()) {
            FileConfiguration config = getConfigFile(player);
            config.set("inventory." + player.getGameMode(), Serialize.get(player));
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private FileConfiguration getConfigFile(Player player) {
        return YamlConfiguration.loadConfiguration(getFile(player));
    }

    

}
