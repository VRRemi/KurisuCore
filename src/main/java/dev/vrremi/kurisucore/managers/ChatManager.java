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

    public void clear() {
        Bukkit.broadcastMessage(StringUtils.repeat(" \n", 1000));
    }

    public void sendStaffChat(String sender, String message) {
        ConfigUtils.staffChat("staff-chat-format", "fubuki.staffchat", new HashMap<String, String>() {{
            put("{sender}", sender);
            put("{message}", message);
        }});
    }

}
