package dev.vrremi.kurisucore.data.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;

public abstract class menu {

    @Getter
    private final String name;
    private final LinkedList<Page> pages = new LinkedList<>();

    public Menu(String name) {
        this.name = name;
        initMenu();
    }

    public void setPage(Page page, int pageNumber) {
        if (pages.size() >= pageNumber) pages.add(page);
        else pages.set(pageNumber, page);
    }

    public Page getPage(int pageNumber) {
        return pages.get(pageNumber);
    }

    

}
