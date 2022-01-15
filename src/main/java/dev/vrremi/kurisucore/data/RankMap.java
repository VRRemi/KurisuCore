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

    

}
