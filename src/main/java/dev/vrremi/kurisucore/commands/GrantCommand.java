package dev.vrremi.kurisucore.commands;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.data.menu.Menus;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import dev.vrremi.kurisucore.utils.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;

public class GrantCommand extends Command {

    public GrantCommand() {
        super("grant", "Grant command", "/grant", Collections.singletonList("grant"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.grant")) {
            if (args.length > 0) {
                Player target = Server.getOnline(args[0]);
                if (target != null) {
                    User user = KurisuCore.getUserManager().getUser((Player) sender);
                    if (user != null) {
                        user.setTarget(target);
                        user.openMenu(Menus.GRANT_MAIN_MENU, 0, false);
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "player-not-online", new HashMap<String, String>() {{
                        put("{player}", args[0]);
                    }});
                }
            } else {
                ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                    put("{usage}", CC.formatPlaceholders("/" + label + " <player>", "&c",
                            "&4"));
                }});
            }
        } else {
            ConfigUtils.sendMessage(sender, "no-permission");
        }
        return true;
    }
}
