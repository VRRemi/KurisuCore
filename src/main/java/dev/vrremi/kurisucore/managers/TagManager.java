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

    private void init() {
        tagList.clear();
        loadTags();
    }

    private void loadTags() {
        FileConfiguration tagsFile = KurisuCore.getConfigManager().getTags();
        for (String tagData : tagsFile.getStringList("tags")) {
            String name = tagData.split(":")[0];
            String tag = tagData.split(":")[1];
            String permission = tagData.split(":")[2];
            tagList.add(new Tag(name, tag, permission));
        }
    }

    public LinkedList<Tag> getAvailableTags(Player player) {
        return tagList.stream().filter(tag -> player.hasPermission(tag.getPermission())).collect(Collectors.toCollection(LinkedList::new));
    }

}
