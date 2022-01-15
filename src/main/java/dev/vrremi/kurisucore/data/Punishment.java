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

    
}
