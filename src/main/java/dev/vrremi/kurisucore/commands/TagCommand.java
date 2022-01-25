package dev.vrremi.kurisucore.commands;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.data.menu.Menus;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TagCommand extends Command {

    public TagCommand() {
        super("tag", "Tag command", "/tag", Arrays.asList("tag", "tags", "prefix", "prefixes"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.tags")) {
}