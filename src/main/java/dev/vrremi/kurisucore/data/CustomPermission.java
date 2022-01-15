package dev.vrremi.kurisucore.data;

import dev.vrremi.kurisucore.KurisuCore;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionBase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomPermission {

    private final Player player;

    public CustomPermissible(Player player) {
        super(player);
        this.player = player;
    }
    
}
