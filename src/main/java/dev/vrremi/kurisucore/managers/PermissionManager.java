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

}
