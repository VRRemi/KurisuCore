package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Tag;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class TagManager {

    private final LinkedList<Tag> tagList = new LinkedList<>();

    public TagManager() {
        init();
    }

    

}
