package dev.vrremi.kurisucore.data;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.menu.Menu;
import dev.vrremi.kurisucore.manager.data.UserDataManager;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class User {

    private final UUID uuid;
    private final String name;
    private final RankMap rankMap;
    private Tag tag;
    private final List<Permission> permissions;
    private final List<Punishment> punishments;

    @Setter
    private String openMenuName;
    @Setter
    private int openMenuPage;
    @Setter
    private Player target;
    @Setter
    private Rank targetRank;
    @Setter
    private Player reply;
    @Setter
    private boolean staffChatHidden;

    private long lastChat;

    public User(Player player, Connection connection) throws SQLException {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        UserDataManager userDataManager = KurisuCore.getUserDataManager();
        this.rankMap = userDataManager.getRanks(uuid, connection);
        this.tag = userDataManager.getTag(uuid, connection);
        this.permissions = userDataManager.getPermissions(uuid, connection);
        this.punishments = userDataManager.getPunishments(uuid, connection);
    }

    public User(UUID uuid, Connection connection) throws SQLException {
        this.uuid = uuid;
        this.name = null;
        UserDataManager userDataManager = KurisuCore.getUserDataManager();
        this.rankMap = userDataManager.getRanks(uuid, connection);
        this.tag = userDataManager.getTag(uuid, connection);
        this.permissions = userDataManager.getPermissions(uuid, connection);
        this.punishments = userDataManager.getPunishments(uuid, connection);
    }

    public void save(Connection connection) throws SQLException {
        UserDataManager userDataManager = KurisuCore.getUserDataManager();
        userDataManager.setRanks(uuid, rankMap, connection);
        userDataManager.setTag(uuid, tag == null ? "" : tag.getName(), connection);
        userDataManager.setPermissions(uuid, permissions, connection);
        userDataManager.setPunishments(uuid, punishments, connection);
    }

    public void resetMenu() {
        openMenuName = null;
        openMenuPage = -1;
        target = null;
        targetRank = null;
    }

    public void openMenu(String menuName, int page, boolean keepData) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Player t = target;
            Rank r = targetRank;
            KurisuCore.getMenuManager().openMenu(player, menuName, page);
            if (keepData) {
                target = t;
                targetRank = r;
            }
        }
    }

    public boolean canChat() {
        return System.currentTimeMillis() >= lastChat;
    }

    public void setLastChat(long delay) {
        lastChat = System.currentTimeMillis() + delay;
    }

    public Menu getOpenMenu() {
        return KurisuCore.getMenuManager().getMenu(openMenuName);
    }

    public Rank getHighestRank() {
        return rankMap == null ? RankMap.getDefault().getHighestRank() : rankMap.getHighestRank();
    }

    public void addRank(Rank rank, long time) {
        rankMap.addRank(rank, time);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            KurisuCore.getNameManager().update(player);
            KurisuCore.getLoopManager().addRank(player, rank);
        }
    }

    public void removeRank(Rank rank) {
        rankMap.removeRank(rank);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            KurisuCore.getNameManager().update(player);
            KurisuCore.getLoopManager().removeRank(player, rank);
        }
    }

    public List<Rank> getRanks() {
        return rankMap == null ? RankMap.getDefault().getActiveRanks() : rankMap.getActiveRanks();
    }

    public List<Rank> getAllRanks() {
        return rankMap == null ? RankMap.getDefault().getAll() : rankMap.getAll();
    }

    

}
