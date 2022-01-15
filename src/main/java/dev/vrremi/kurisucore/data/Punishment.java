package dev.vrremi.kurisucore.data;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Punishment {

    private final String id;
    private final PunishmentType punishmentType;
    private final String reason, punisher;
    private final long created, timeout;

    public Punishment(PunishmentType punishmentType, String reason, String punisher, long created, long timeout) {
        this(null, punishmentType, reason, punisher, created, timeout);
    }

    public Punishment(String id, PunishmentType punishmentType, String reason, String punisher, long created,
                      long timeout) {
        String random = getNewId();
        if (id == null)
            this.id = random;
        else
            this.id = id;
        this.punishmentType = punishmentType;
        this.reason = reason;
        this.punisher = punisher;
        this.created = created;
        this.timeout = timeout;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() > timeout;
    }

    
}
