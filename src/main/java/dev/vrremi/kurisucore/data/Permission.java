package dev.vrremi.kurisucore.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Permission {

    private final String node;
    private final long timeout;

    public boolean hasTimedOut() {
        return System.currentTimeMillis() > timeout;
    }

    public boolean isActive() {
        return System.currentTimeMillis() < timeout;
    }

    public String toString() {
        return "{Permission:node=" + node + ",timeout=" + timeout + "}";
    }

}
