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



}
