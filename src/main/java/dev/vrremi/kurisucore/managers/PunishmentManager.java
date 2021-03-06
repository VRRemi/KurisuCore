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

    public Punishment getLastBan(UUID uuid) {
        return getLastBan(KurisuCore.getUserManager().getUser(uuid));
    }

    public Punishment getLastBan(Player player) {
        return getLastBan(KurisuCore.getUserManager().getUser(player));
    }

    public Punishment getLastBan(User user) {
        if (user != null) {
            List<Punishment> punishments = user.getPunishments().stream().filter(p -> !p.hasExpired()).collect(Collectors.toList());
            for (int i = punishments.size() - 1; i >= 0; i--) {
                if (Arrays.asList(PunishmentType.BAN, PunishmentType.TEMP_BAN).contains(punishments.get(i).getPunishmentType())) {
                    return punishments.get(i);
                }
            }
        }
        return null;
    }

    public boolean isUserMuted(Player player) {
        User user = KurisuCore.getUserManager().getUser(player);
        if (user != null) {
            List<Punishment> punishments = user.getPunishments().stream().filter(p -> !p.hasExpired()).collect(Collectors.toList());
            for (int i = punishments.size() - 1; i >= 0; i--) {
                if (Arrays.asList(PunishmentType.MUTE, PunishmentType.TEMP_MUTE).contains(punishments.get(i).getPunishmentType())) {
                    return true;
                } else if (punishments.get(i).getPunishmentType().equals(PunishmentType.UNMUTE)) {
                    return false;
                }
            }
        }
        return false;
    }

    public Punishment getLastMute(Player player) {
        User user = KurisuCore.getUserManager().getUser(player);
        if (user != null) {
            List<Punishment> punishments = user.getPunishments().stream().filter(p -> !p.hasExpired()).collect(Collectors.toList());
            for (int i = punishments.size() - 1; i >= 0; i--) {
                if (Arrays.asList(PunishmentType.MUTE, PunishmentType.TEMP_MUTE).contains(punishments.get(i).getPunishmentType())) {
                    return punishments.get(i);
                }
            }
        }
        return null;
    }


}
