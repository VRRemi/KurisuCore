package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.data.Tag;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.utils.CC;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIManager extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "kurisu";
    }

    @Override
    public @NotNull String getAuthor() {
        return "VRRemi";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
        if (offlinePlayer.isOnline() && player != null) {
            User user = KurisuCore.getUserManager().getUser(player);
            if (user != null) {
                Rank rank = user.getHighestRank();
                Tag tag = user.getTag();
                if (params.equalsIgnoreCase("tag_display")) {
                    return tag == null ? "" : tag.getTag();
                } else if (params.equalsIgnoreCase("tag_name")) {
                    return tag == null ? "" : tag.getName();
                } else if (params.equalsIgnoreCase("rank_prefix")) {
                    return rank == null ? "" : rank.getPrefix();
                } else if (params.equalsIgnoreCase("rank_suffix")) {
                    return rank == null ? "" : rank.getSuffix();
                } else if (params.equalsIgnoreCase("rank_color")) {
                    return rank == null ? "" : rank.getColor();
                } else if (params.equalsIgnoreCase("rank_priority")) {
                    return rank == null ? "" : CC.format(rank.getPriority());
                } else if (params.equalsIgnoreCase("rank_name")) {
                    return rank == null ? "" : rank.getName();
                }
            }
        }
        return null;
    }

}
