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

    public boolean isUserBanned(UUID uuid) {
        return isUserBanned(KurisuCore.getUserManager().getUser(uuid));
    }

    public boolean isUserBanned(User user) {
        if (user != null) {
            List<Punishment> punishments = user.getPunishments().stream().filter(p -> !p.hasExpired()).collect(Collectors.toList());
            for (int i = punishments.size() - 1; i >= 0; i--) {
                if (Arrays.asList(PunishmentType.BAN, PunishmentType.TEMP_BAN).contains(punishments.get(i).getPunishmentType())) {
                    return true;
                } else if (punishments.get(i).getPunishmentType().equals(PunishmentType.UNBAN)) {
                    return false;
                }
            }
        }
        return false;
    }

    

}
