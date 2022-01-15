package dev.vrremi.kurisucore.data;

import org.apache.commons.lang.WordUtils;

public enum PunishmentType {

    BAN,
    TEMP_BAN,
    UNBAN,
    MUTE,
    TEMP_MUTE,
    UNMUTE,
    KICK,
    WARN;

    public String readableName() {
        return WordUtils.capitalize(this.name().replace("_", "-").toLowerCase());
    }
    
}
