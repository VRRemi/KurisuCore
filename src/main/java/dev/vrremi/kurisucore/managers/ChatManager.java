package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.utils.ConfigUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

import java.util.HashMap;

@Getter
@Setter
public class ChatManager {

    private boolean muted;
    private double delay;

    public ChatManager() {
        init();
    }

    private void init() {
        muted = false;
        delay = 0;
    }

}
