package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.utils.Threading;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, User> userMap = new HashMap<>();

    public UserManager() {
        init();
    }

    private void init() {
        userMap.clear();
        Threading.runAsync(() -> {
            Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
            try {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    cache(online, connection);
                }
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                ConnectionPoolManager.close(connection);
            }
        });
    }

    public void cache(Player player, connection) throws SQLException {
        
    }
}
