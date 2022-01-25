package dev.vrremi.kurisucore.menus;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Tag;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.data.menu.Menu;
import dev.vrremi.kurisucore.data.menu.Menus;
import dev.vrremi.kurisucore.data.menu.Page;
import dev.vrremi.kurisucore.data.menu.SlotAction;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import dev.vrremi.kurisucore.utils.LineUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TagsMenu extends Menu {

    public TagsMenu() {
        super(Menus.TAGS_MENU);
    }

    @Override
    public void initMenu() {
        setPage(new Page() {
            @Override
            public Inventory getInventory(Player player) {
                Inventory inventory = Bukkit.createInventory(null, 54, CC.color("&b&lTAGS"));
