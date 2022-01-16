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
            Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
            try {
                rankList.addAll(KurisuCore.getRankDataManager().getAllRanks(connection));
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPoolManager.close(connection);
            }
        });
    }

    public Rank getRank(String name) {
        if (name == null || name.isEmpty()) return null;
        Rank rank = null;
        for (Rank r : rankList) {
            if (r.getName().equalsIgnoreCase(name)) {
                rank = r;
            }
        }
        return rank;
    }

    public List<Rank> getRanks() {
        return rankList;
    }

    public List<Rank> getSortedRanks() {
        List<Rank> sortedList = new ArrayList<>(rankList);
        sortedList.sort(Comparator.comparingInt(Rank::getPriority).reversed());
        return sortedList;
    }

    public void updateRank(String name, Connection connection) throws SQLException {
        deleteRank(name);
        rankList.add(KurisuCore.getRankDataManager().getRank(name, connection));
        Bukkit.getOnlinePlayers().forEach(KurisuCore.getNameManager()::update);
    }

    public void deleteRank(String name) {
        Rank rank = getRank(name);
        if (rank != null) {
            rankList.remove(rank);
        }
    }

}
