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
        return FubukiCore.getMenuManager().getMenu(openMenuName);
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

    public long getRankExpireTime(Rank rank) {
        return rankMap.getTime(rank.getName());
    }

    public void setTag(Tag tag) {
        this.tag = tag;
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            KurisuCore.getNameManager().update(player);
    }

    public void resetTag() {
        setTag(null);
    }

    public LinkedList<Tag> getAvailableTags() {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            return KurisuCore.getTagManager().getAvailableTags(player);
        return new LinkedList<>();
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            KurisuCore.getPermissionManager().update(player);
            KurisuCore.getLoopManager().addPermission(player, permission);
        }
    }

    public void removePermission(String permission) {
        List<Permission> permissions = new ArrayList<>();
        for (Permission perm : this.permissions) {
            if (perm.getNode().equalsIgnoreCase(permission)) {
                permissions.add(perm);
            }
        }
        this.permissions.removeAll(permissions);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            KurisuCore.getPermissionManager().update(player);
            permissions.forEach(perm -> {
                KurisuCore.getLoopManager().removePermission(player, perm);
            });
        }
    }

    public void addPunishment(Punishment punishment) {
        punishments.add(punishment);
    }

    public void removePunishment(String id) {
        Punishment punishment = null;
        for (Punishment p : punishments) {
            if (p.getId().equalsIgnoreCase(id)) {
                punishment = p;
            }
        }
        if (punishment != null) {
            punishments.remove(punishment);
        }
    }

    public boolean hasPunishment(String id) {
        for (Punishment p : punishments) {
            if (p.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public void ban(String reason, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.BAN, reason, punisher, System.currentTimeMillis(),
                Long.MAX_VALUE);
        addPunishment(punishment);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.kickPlayer(CC.color(ConfigUtils.getBanMessage(punishment)));
        }
    }

    public void tempBan(String reason, String punisher, long timeout) {
        Punishment punishment = new Punishment(PunishmentType.TEMP_BAN, reason, punisher, System.currentTimeMillis(),
                System.currentTimeMillis() + timeout);
        addPunishment(punishment);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.kickPlayer(CC.color(ConfigUtils.getBanMessage(punishment)));
        }
    }

    public void unban(String reason, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.UNBAN, reason, punisher, System.currentTimeMillis(),
                Long.MAX_VALUE);
        addPunishment(punishment);
    }

    public void mute(String reason, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.MUTE, reason, punisher, System.currentTimeMillis(),
                Long.MAX_VALUE);
        addPunishment(punishment);
    }

    public void tempMute(String reason, String punisher, long timeout) {
        Punishment punishment = new Punishment(PunishmentType.TEMP_MUTE, reason, punisher, System.currentTimeMillis(),
                System.currentTimeMillis() + timeout);
        addPunishment(punishment);
    }

    public void unmute(String reason, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.UNMUTE, reason, punisher, System.currentTimeMillis(),
                Long.MAX_VALUE);
        addPunishment(punishment);
    }

    public void kick(String reason, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.KICK, reason, punisher, System.currentTimeMillis(),
                Long.MAX_VALUE);
        addPunishment(punishment);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.kickPlayer(CC.color(ConfigUtils.getKickMessage(punishment)));
        }
    }

    public void warn(String reason, String punisher) {
        Punishment punishment = new Punishment(PunishmentType.WARN, reason, punisher, System.currentTimeMillis(),
                Long.MAX_VALUE);
        addPunishment(punishment);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.sendMessage(CC.color(ConfigUtils.getWarnMessage(punishment)));
        }
    }

    

}
