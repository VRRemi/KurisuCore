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
                fill(inventory);
                User user  = KurisuCore.getUserManager().getUser(player);
                if (user == null) return inventory;
                LinkedList<Tag> tags = user.getAvailableTags();
                int index = 0;
                for (Tag tag : tags) {
                    if (index == 49) {
                        index++;
                        continue;
                    }
                    ItemStack item = new ItemStack(Material.NAME_TAG);
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(CC.color(tag.getTag()));
                    List<String> lines = new ArrayList<>();
                    LineUtils.addHeader(lines, player);
                    lines.add("&7Click to apply the &b&n" + tag.getName() + "&7 tag");
                    LineUtils.addHeader(lines, player);
                    meta.setLore(CC.color(lines));
                    if (user.getTag() == tag) {
                        meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }
                    meta.addItemFlags(ItemFlag.values());
                    item.setItemMeta(meta);
                    inventory.setItem(index, item);
                    setAction(index, SlotAction.APPLY_TAG);
                    index++;
                }
                ItemStack reset = new ItemStack(Material.ANVIL);
                ItemMeta resetMeta = reset.getItemMeta();
                assert resetMeta != null;
                resetMeta.setDisplayName(CC.color("&b&lRESET TAG"));
                List<String> lines = new ArrayList<>();
                LineUtils.addHeader(lines, player);
                lines.add("&7Click reset your tag");
                LineUtils.addHeader(lines, player);
                resetMeta.setLore(CC.color(lines));
                reset.setItemMeta(resetMeta);
                inventory.setItem(49, reset);
                setAction(49, SlotAction.RESET_TAG);
                return inventory;
            }

            @Override
            public void onClick(InventoryClickEvent event) {
                Player player = (Player) event.getWhoClicked();
