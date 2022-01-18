package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private File configFile, messageFile, tagsFile;
    private FileConfiguration config, messages, tags;

    private final Map<String, String> messageMap = new HashMap<>();
    private final Map<String, List<String>> multiMessageMap = new HashMap<>();

    public ConfigManager() {
        loadConfigs();
        loadMessages();
    }

    private void loadConfigs() {
        KurisuCore plugin = KurisuCore.getInstance();
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();
        configFile = new File(plugin.getDataFolder(), "config.yml");
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        tagsFile = new File(plugin.getDataFolder(), "tags.yml");
        InputStream configStream = plugin.getResource("config.yml");
        InputStream messagesStream = plugin.getResource("messages.yml");
        InputStream tagsStream = plugin.getResource("tags.yml");
        try {
            if (!configFile.exists()) {
                assert configStream != null;
                Files.copy(configStream, configFile.toPath());
            }
            
    }



}
