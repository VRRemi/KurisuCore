package dev.vrremi.kurisucore.data;

import dev.vrremi.kurisucore.KurisuCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RankMap {

    private final Map<Rank, Long> rankMap = new HashMap<>();

    public void addRank(Rank rank, long time) {
        rankMap.put(rank, time);
    }

    public void removeRank(Rank rank) {
        rankMap.remove(rank);
    }

    public long getTime(String name) {
        return rankMap.get(KurisuCore.getRankManager().getRank(name));
    }

    public String convertToString() {
        StringBuilder sb = new StringBuilder();
        rankMap.forEach((rank, time) -> {
            if (time > System.currentTimeMillis())
                sb.append(rank.getName()).append(":").append(time == Long.MAX_VALUE ? "0" : time).append(",");
        });
        return sb.substring(0, sb.length() - 1);
    }

    public List<Rank> getActiveRanks() {
        return rankMap.keySet().stream().filter(rank -> rankMap.get(rank) > System.currentTimeMillis()).collect(Collectors.toList());
    }

    public List<Rank> getAll() {
        return new ArrayList<>(rankMap.keySet());
    }

    public Rank getHighestRank() {
        Rank highestRank = null;
        int highestPriority = Integer.MIN_VALUE;
        for (Rank rank : getActiveRanks()) {
            if (rank.getPriority() > highestPriority) {
                highestRank = rank;
                highestPriority = rank.getPriority();
            }
        }
        return highestRank;
    }

    public static RankMap convertFromString(String string) {
        RankMap rankMap = new RankMap();
        if (string.isEmpty()) return getDefault();
        for (String rankPart : string.split(",")) {
            if (rankPart.isEmpty()) continue;
            String name = rankPart.split(":")[0];
            long time = Long.parseLong(rankPart.split(":")[1]);
            Rank rank = KurisuCore.getRankManager().getRank(name);
            if (rank != null) {
                rankMap.addRank(rank, time == 0 ? Long.MAX_VALUE : time);
            }
        }
        return rankMap;
    }

    public static RankMap getDefault() {
        RankMap rankMap = new RankMap();
        KurisuCore.getRankManager().getRanks().stream().filter(Rank::isDefaultRank).forEach(rank -> rankMap.addRank(rank,
                Long.MAX_VALUE));
        return rankMap;
    }

    public String toString() {
        return "{RankMap=" + rankMap + "}";
    }

}
