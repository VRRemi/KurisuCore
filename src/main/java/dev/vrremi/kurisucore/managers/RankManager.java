package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.utils.Threading;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankManager {

    private final List<Rank> rankList = new ArrayList<>();

    public RankManager() {
        init();
    }

    private void init() {
        rankList.clear();
        loadRanks();
    }

    private void loadRanks() {
        rankList.clear();
        Threading.runAsync(() -> {
            Connection connection = FubukiCore.getConnectionPoolManager().getConnection();
            try {
                rankList.addAll(FubukiCore.getRankDataManager().getAllRanks(connection));
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPoolManager.close(connection);
            }
        });
    }

    

}
