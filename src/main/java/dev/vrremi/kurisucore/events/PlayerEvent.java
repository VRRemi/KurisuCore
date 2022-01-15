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

    @EventHandler
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

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Threading.runAsync(() -> {
            Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
            try {
                KurisuCore.getUserManager().remove(player, connection);
                KurisuCore.getPermissionManager().remove(player);
                KurisuCore.getLoopManager().remove(player);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPoolManager.close(connection);
            }
        });
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = KurisuCore().getUser(player);
        if (user != null) {
            if (KurisuCore.getPunishmentManager().isUserMuted(player)) {
                event.setCancelled(true);
                player.sendMessage(ConfigUtils.getMuteMessage(KurisuCore.getPunishmentManager().getLastMute(player)));
            } else {
                if (KurisuCore.getChatManager().isMuted()) {
                    if (!player.hasPermission("kurisu.admin")) {
                        event.setCancelled(true);
                        ConfigUtils.sendMessage(player, "chat-muted-user");
                        return;
                    }
                } else if (KurisuCore.getChatManager().getDelay() > 0) {
                    if (!player.hasPermission("kurisu.admin")) {
                        if (user.canChat()) {
                            user.setLastChat((long) (KurisuCore.getChatManager().getDelay() * 1000));
                        } else {
                            event.setCancelled(true);
                            double delay = (user.getLastChat() - System.currentTimeMillis()) / 1000D;
                            String timeLeft = String.format("%.1f", delay);
                            ConfigUtils.sendMessage(player, "chat-delayed-user", new HashMap<String, String>() {{
                                put("{time-left}", timeLeft);
                            }});
                            return;
                        }
                    }
                }
                if (event.getMessage().startsWith("!")) {
                    if (player.hasPermission("kurisu.staffchat")) {
                        event.setCancelled(true);
                        KurisuCore.getChatManager().sendStaffChat(player.getName(), event.getMessage().substring(1));
                        return;
                    }
                }
                Rank rank = user.getHighestRank();
                Tag tag = user.getTag();
                String name = "%s";
                if (rank != null) {
                    if (tag != null) {
                        name = tag.getTag() + rank.getColor() + "%s" + rank.getSuffix();
                    } else {
                        name = rank.getPrefix() + rank.getColor() + "%s" + rank.getSuffix();
                    }
                } else {
                    if (tag != null) {
                        name = tag.getTag() + "%s";
                    }
                }
                event.setFormat(CC.color(name + "&7: &r") + "%s");
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            User user = KurisuCore.getUserManager().getUser(player);
            if (user == null) return;
            if (event.getClick().equals(ClickType.DOUBLE_CLICK)) return;
            if (event.getCurrentItem() == null) return;
            if (event.getClickedInventory() == null) return;
            if (!event.getClickedInventory().getType().equals(InventoryType.CHEST)) return;
            Menu menu = user.getOpenMenu();
            if (menu != null) {
                event.setCancelled(true);
                menu.getPage(user.getOpenMenuPage()).onClick(event);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            User user = KurisuCore.getUserManager().getUser(player);
            if (user == null) return;
            user.resetMenu();
        }
    }

}