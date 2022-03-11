package dev.vrremi.kurisucore.commands;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import dev.vrremi.kurisucore.utils.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick", "Kick command", "/kick", Collections.singletonList("kick"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.kick")) {
            if (args.length > 0) {
                String punisher = sender instanceof Player ? sender.getName() : "Console";
                Player target = Server.getOnline(args[0]);
                String reason = "";
                if (args.length > 1) {
                    StringBuilder reasonSB = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        reasonSB.append(args[i]).append(" ");
                    }
                    reason = reasonSB.substring(0, reasonSB.length() - 1);
                }
                String finalReason = reason;
                if (target != null) {
                    User user = KurisuCore.getUserManager().getUser(target);
                    if (user != null) {
                        user.kick(reason, punisher);
                        ConfigUtils.sendMessage(sender, "user-kicked", new HashMap<String, String>() {{
                            put("{player}", target.getName());
                            put("{reason}", finalReason.isEmpty() ? "No reason provided" : finalReason);
                        }});
                    } else {
                        ConfigUtils.sendMessage(sender, "error-kicking", new HashMap<String, String>() {{
                            put("{player}", target.getName());
                        }});
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "player-not-online", new HashMap<String, String>() {{
                        put("{player}", args[0]);
                    }});
                }
            } else {
                ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                    put("{usage}", CC.formatPlaceholders("/" + label + " <player> [reason]", "&c",
                            "&4"));
                }});
            }
        } else {
            ConfigUtils.sendMessage(sender, "no-permission");
        }
        return true;
    }

}
