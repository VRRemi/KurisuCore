package dev.vrremi.kurisucore;

import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.managers.*;
import dev.vrremi.kurisucore.managers.data.RankDataManager;
import dev.vrremi.kurisucore.managers.data.UserDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class KurisuCore extends JavaPlugin {

    private static KurisuCore instance;
    
    private static ConfigManager configManager;
    private static ConnectionPoolManager connectionPoolManager;
    
    private static UserDataManager userDataManager;
    private static RankDataManager rankDataManager;
    
    private static RankManager rankManager;
    private static UserManager userManager;
    private static TagManager tagManager;
    
    private static PermissionManager permissionManager;
    private static PunishmentManager punishmentManager;
    private static GameModeManager gameModeManager;
    private static NameManager nameManager;
    private static LoopManager loopManager;
    private static MenuManager menuManager;
    private static ChatManager chatManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        
        configManager = new ConfigManager();
        connectionPoolManager = new ConnectionPoolManager;
        
        new TableManager(connectionPoolManager.getConnection());

        userDataManager = new UserDataManager();
        rankDataManager = new RankDataManager();
        
        rankManager = new RankManager();
        userManager = new UserManager();
        tagManager = new TagManager();
        
        permissionManager = new PermissionManager();
        punishmentManager = new PunishmentManager();
        gameModeManager = new GameModeManager();
        nameManager = new NameManager();
        loopManager = new LoopManager();
        menuManager = new MenuManager();
        chatManager = new ChatManager();
        
        new CommandManager();
        new EventManager();
        
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIManager().register();
        } 
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (connectionPoolManager != null) {
            Connection connection = connectionPoolManager.getConnection();
            try {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    User user =
                }
            }
        }
    }
}
