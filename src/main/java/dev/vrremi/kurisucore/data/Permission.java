package dev.vrremi.kurisucore.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Permission {

    private final String node;
    private final long timeout;

    
}
