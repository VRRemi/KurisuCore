package dev.vrremi.kurisucore.data.menu;

import dev.vrremi.kurisucore.utils.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public abstract class page {

    private final Map<Integer, SlotAction> slotActionMap = new HashMap<>();

    private final ItemStack BACKGROUND;

    public Page() {
        ItemStack background = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta backgroundMeta = background.getItemMeta();
        assert backgroundMeta != null;
        backgroundMeta.setDisplayName(CC.color("&0"));
        background.setItemMeta(backgroundMeta);
        BACKGROUND = background;
    }

    public abstract Inventory getInventory(Player player);

    public abstract void onClick(InventoryClickEvent event);

    

}
