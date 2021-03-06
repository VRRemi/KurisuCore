package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.data.menu.Menu;
import dev.vrremi.kurisucore.menus.GrantDurationMenu;
import dev.vrremi.kurisucore.menus.GrantMainMenu;
import dev.vrremi.kurisucore.menus.TagsMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {

    private final Map<String, Menu> menuMap = new HashMap();

    public MenuManager() {
        initMenus();
    }

    private void initMenus() {
        menuMap.clear();
        register(new TagsMenu());
        register(new GrantMainMenu());
        register(new GrantDurationMenu());
    }

    public void register(Menu menu) {
        menuMap.put(menu.getName(), menu);
    }

    public Menu getMenu(String name) {
        return menuMap.get(name);
    }

    public void openMenu(Player player, String name, int page) {
        User user = KurisuCore.getUserManager().getUser(player);
        if (user != null) {
            if (getMenu(name) == null) return;
            player.openInventory(getMenu(name).getPageInventory(player, page));
            user.setOpenMenuName(name);
            user.setOpenMenuPage(page);
        }
    }

}
