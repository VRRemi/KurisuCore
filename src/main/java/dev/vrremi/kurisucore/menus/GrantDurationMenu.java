package dev.vrremi.kurisucore.menus;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.data.menu.Menu;
import dev.vrremi.kurisucore.data.menu.Menus;
import dev.vrremi.kurisucore.data.menu.Page;
import dev.vrremi.kurisucore.data.menu.SlotAction;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import dev.vrremi.kurisucore.utils.LineUtils;
import dev.vrremi.kurisucore.utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GrantDurationMenu extends Menu {

    public GrantDurationMenu() {
        super(Menus.GRANT_DURATION_MENU);
    }

    @Override
    public void initMenu() {
        setPage(new Page() {
            @Override
            public Inventory getInventory(Player player) {
                Inventory inventory = Bukkit.createInventory(null, 27, CC.color("&b&lGRANT DURATION"));
                User user = KurisuCore.getUserManager().getUser(player);
                if (user == null) return inventory;
                if (user.getTarget() == null) return inventory;
                if (user.getTargetRank() == null) return inventory;
                fill(inventory);
                List<String> names = Arrays.asList("&4Permanent", "&a7 days", "&e1 month", "&c3 months");
                List<Material> materials = Arrays.asList(Material.REDSTONE_BLOCK, Material.GREEN_WOOL,
                        Material.YELLOW_WOOL, Material.RED_WOOL);
                List<Integer> slots = Arrays.asList(10, 12, 14, 16);
                List<SlotAction> slotActions = Arrays.asList(SlotAction.APPLY_PERMANENT, SlotAction.APPLY_7_DAYS,
                        SlotAction.APPLY_1_MONTH, SlotAction.APPLY_3_MONTH);
                for (int i = 0; i < 4; i++) {
                    ItemStack item = new ItemStack(materials.get(i));
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(CC.color(names.get(i)));
                    List<String> lines = new ArrayList<>();
                    LineUtils.addHeader(lines, player);
                    String name = user.getTarget() == null ? "null" : user.getTarget().getName();
                    lines.add("&7Click to grant &b&n" + name + "&r &7with " + user.getTargetRank().getColor() + user.getTargetRank().getName());
                    LineUtils.addHeader(lines, player);
                    meta.setLore(CC.color(lines));
                    item.setItemMeta(meta);
                    inventory.setItem(slots.get(i), item);
                    setAction(slots.get(i), slotActions.get(i));
                }
}
