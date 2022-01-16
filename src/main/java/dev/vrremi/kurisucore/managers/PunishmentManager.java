package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Punishment;
import dev.vrremi.kurisucore.data.PunishmentType;
import dev.vrremi.kurisucore.data.User;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PunishmentManager {

    public boolean isUserBanned(Player player) {
        return isUserBanned(KurisuCore.getUserManager().getUser(player));
    }

    

}
