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

    

}
