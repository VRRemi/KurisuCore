package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.data.Tag;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NameManager {

    public NameManager() {
        init();
    }

    private void init() {
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

    public void update(Player player) {
        User user = KurisuCore.getUserManager().getUser(player);
        if (user != null) {
            Rank rank = user.getHighestRank();
            Tag tag = user.getTag();
            String listName = player.getName();
            if (rank != null) {

                if (tag != null) {
                    listName = tag.getTag() + rank.getColor() + player.getName() + rank.getSuffix();

}
