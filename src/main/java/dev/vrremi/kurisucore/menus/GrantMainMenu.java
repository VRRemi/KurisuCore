package dev.vrremi.kurisucore.menus;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.data.menu.Menu;
import dev.vrremi.kurisucore.data.menu.Menus;
import dev.vrremi.kurisucore.data.menu.Page;
import dev.vrremi.kurisucore.data.menu.SlotAction;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.LineUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GrantMainMenu extends Menu {

    public GrantMainMenu() {
        super(Menus.GRANT_MAIN_MENU);
    }
}
