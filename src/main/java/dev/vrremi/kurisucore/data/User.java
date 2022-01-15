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

    
}
