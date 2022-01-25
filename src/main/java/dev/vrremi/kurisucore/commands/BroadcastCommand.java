package dev.vrremi.kurisucore.commands;


import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

public class BroadcastCommand extends Command {

    public BroadcastCommand() {
        super("broadcast", "Broadcast command", "/broadcast", Arrays.asList("broadcast", "bc"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.broadcast")) {
            if (args.length > 0) {
                StringBuilder messageSB = new StringBuilder();
                for (String arg : args) {
                    messageSB.append(arg).append(" ");
                }
                String message = messageSB.substring(0, messageSB.length() - 1);
                String format = KurisuCore.getConfigManager().getMessage("broadcast-format").replace("{message}", message);
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(CC.color(format)));
            } else {
                ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                    put("{usage}", CC.formatPlaceholders("/" + label + " <message>", "&c", "&4"));
                }});
    }
}
