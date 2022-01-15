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

    public void cache(Player player, Connection connection) throws SQLException {
        userMap.put(player.getUniqueId(), new User(player, connection));
        KurisuCore.getLoopManager().add(player);
    }

    public void cache(UUID uuid, Connection connection) throws SQLException {
        userMap.put(uuid, new User(uuid, connection));
    }

    public void remove(Player player, Connection connection) throws SQLException {
        User user = getUser(player);
        if (user != null) {
            user.save(connection);
            userMap.remove(player.getUniqueId());
        }
    }
    
}
