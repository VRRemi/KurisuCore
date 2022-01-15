package dev.vrremi.kurisucore.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Rank {

    private final String name;
    private final int priority;
    private final String prefix, suffix, color;
    private final List<Permission> permissions;
    private final boolean defaultRank;

    
}
