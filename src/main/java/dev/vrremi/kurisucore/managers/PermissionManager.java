package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.CustomPermissible;
import dev.vrremi.kurisucore.data.Permission;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.utils.Threading;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PermissionManager {

    private final HashMap<UUID, PermissionAttachment> permissionMap = new HashMap<>();
    private final HashMap<UUID, List<String>> permissionNodeMap = new HashMap<>();

    public PermissionManager() {
        init();
    }

    private void init() {
        permissionMap.clear();
        Bukkit.getOnlinePlayers().forEach(this::add);
    }

    public void add(Player player) {
        injectPermissible(player);
        PermissionAttachment permissionAttachment = player.addAttachment(KurisuCore.getInstance());
        permissionMap.put(player.getUniqueId(), permissionAttachment);
        update(player);
    }

    public void update(Player player) {
        List<String> permissions = new ArrayList<>();
        if (permissionMap.get(player.getUniqueId()) == null) {
            add(player);
        }
        PermissionAttachment permissionAttachment = permissionMap.get(player.getUniqueId());
        User user = KurisuCore.getUserManager().getUser(player);
        permissionAttachment.getPermissions().forEach((perm, active) -> permissionAttachment.unsetPermission(perm));
        for (Permission permission : user.getPermissions()) {
            if (permission.isActive()) {
                permissionAttachment.setPermission(permission.getNode(), true);
                permissions.add(permission.getNode());
            }
        }
        for (Rank rank : user.getRanks()) {
            for (Permission permission : rank.getPermissions()) {
                permissionAttachment.setPermission(permission.getNode(), true);
                permissions.add(permission.getNode());
            }
        }
        permissionNodeMap.put(player.getUniqueId(), permissions);
        permissionMap.put(player.getUniqueId(), permissionAttachment);
        recalculate(player);
    }

    public void remove(Player player) {
        permissionMap.remove(player.getUniqueId());
        permissionNodeMap.remove(player.getUniqueId());
    }

    private void recalculate(Player player) {
        Threading.runSync(() -> {
            player.recalculatePermissions();
            player.updateCommands();
        });
    }

    public List<String> getPermissions(Player player) {
        return permissionNodeMap.get(player.getUniqueId());
    }

    private void injectPermissible(Player player) {
        try {
            Field field = CraftHumanEntity.class.getDeclaredField("perm");
            field.setAccessible(true);
            field.set(player, new CustomPermissible(player));
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }





}
