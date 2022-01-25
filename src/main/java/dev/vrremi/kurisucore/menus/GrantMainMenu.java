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

    @Override
    public void initMenu() {
        setPage(new Page() {
            @Override
            public Inventory getInventory(Player player) {
                Inventory inventory = Bukkit.createInventory(null, 54, CC.color("&b&lGRANT MENU"));
                User user = KurisuCore.getUserManager().getUser(player);
                if (user == null) return inventory;
                fill(inventory);
                int index = 0;
                List<Rank> rankList = KurisuCore.getRankManager().getSortedRanks();
                for (Rank rank : rankList) {
                    ItemStack item = new ItemStack(CC.getMaterialFromColor(rank.getColor()));
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(CC.color(rank.getColor() + rank.getName()));
                    List<String> lines = new ArrayList<>();
                    LineUtils.addHeader(lines, player);
                    String name = user.getTarget() == null ? "null" : user.getTarget().getName();
                    lines.add("&7Click to grant &b&n" + name + "&r &7with " + rank.getColor() + rank.getName());
                    LineUtils.addHeader(lines, player);
                    meta.setLore(CC.color(lines));
                    item.setItemMeta(meta);
                    inventory.setItem(index, item);
                    setAction(index, SlotAction.APPLY_RANK);
                    index++;
                }

                return inventory;
            }

            @Override
            public void onClick(InventoryClickEvent event) {
                Player player = (Player) event.getWhoClicked();
                User user = KurisuCore.getUserManager().getUser(player);
                if (user == null) return;
                SlotAction slotAction = getAction(event.getSlot());
                }
            }
        }, 0);
    }

}