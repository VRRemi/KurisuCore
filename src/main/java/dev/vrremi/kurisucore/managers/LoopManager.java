package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Permission;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.data.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LoopManager {

    private long autoSaveTime;

    private final Map<UUID, List<Permission>> permissionMap = new HashMap<>();
    private final Map<UUID, List<Rank>> rankMap = new HashMap<>();

    public LoopManager() {
        startLoop();
    }

    private void startLoop() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            long current = System.currentTimeMillis();
            if (autoSaveTime <= current) {
                Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();
                if (playerList.size() > 0) {
                    UserManager userManager = KurisuCore.getUserManager();
                    Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
                    try {
                        for (Player player : playerList) {
                            User user = userManager.getUser(player);
                            if (user != null) {
                                user.save(connection);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        ConnectionPoolManager.close(connection);
                    }
                }
                autoSaveTime = current + 60000L;
            }
            for (Player online : Bukkit.getOnlinePlayers()) {
                User user = KurisuCore.getUserManager().getUser(online);
                if (permissionMap.containsKey(online.getUniqueId())) {
                    List<Permission> finalPermissions =
                            permissionMap.get(online.getUniqueId()).stream().filter(Permission::isActive).collect(Collectors.toList());
                    if (finalPermissions.size() != permissionMap.get(online.getUniqueId()).size()) {
                        KurisuCore.getPermissionManager().update(online);
                        KurisuCore.getNameManager().update(online);
                    }
                    permissionMap.put(online.getUniqueId(), finalPermissions);
                }
                if (rankMap.containsKey(online.getUniqueId())) {
                    List<Rank> finalRanks =
                            rankMap.get(online.getUniqueId()).stream().filter(rank -> user.getRankExpireTime(rank) > System.currentTimeMillis()).collect(Collectors.toList());
                    if (finalRanks.size() != rankMap.get(online.getUniqueId()).size()) {
                        KurisuCore.getPermissionManager().update(online);
                        KurisuCore.getNameManager().update(online);
                    }
                    rankMap.put(online.getUniqueId(), finalRanks);
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }


}
