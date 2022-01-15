package dev.vrremi.kurisucore.events;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Punishment;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.data.Tag;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.data.menu.Menu;
import dev.vrremi.kurisucore.managers.ConnectionPoolManager;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import dev.vrremi.kurisucore.utils.Threading;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerEvent implements Listener {

    @Eventhandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
        try {
            if (KurisuCore.getUserDataManager().playerExists(uuid, connection)) {
                User user = new User(uuid, connection);
                if (KurisuCore.getPunishmentManager().isUserBanned(user)) {
                    Punishment punishment = KurisuCore.getPunishmentManager().getLastBan(user);
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.color(ConfigUtils.getBanMessage(punishment)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPoolManager.close(connection);
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            Threading.runAsync(() -> {
                Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
                try {
                    KurisuCore.getUserDataManager().create(player, connection);
                    KurisuCore.getUserManager().cache(player, connection);
                    KurisuCore.getPermissionManager().add(player);
                    KurisuCore.getNameManager().update(player);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    ConnectionPoolManager.close(connection);
                }
            });
        }
    }

    
}
