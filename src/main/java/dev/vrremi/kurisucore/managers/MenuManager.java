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

    

}
