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

    @Override
    public boolean hasPermission(@NotNull String inName) {
        List<String> permList = KurisuCore.getPermissionManager().getPermissions(player);
        if (permList.contains("*") || super.isOp()) {
            return true;
        } else {
            for (String permission : permList) {
                if (permission.endsWith(".*")) {
                    if (inName.startsWith(permission.split("\\.\\*")[0])) {
                        return true;
                    }
                }
            }
        }
        return super.hasPermission(inName);
    }

}
