package dev.vrremi.kurisucore.commands;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.managers.ChatManager;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import dev.vrremi.kurisucore.utils.LineUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ChatManagerCommand extends Command {

    public ChatManagerCommand() {
        super("chatmanager", "Chat Manager command", "/chatmanager", Arrays.asList("chatmanager", "chatm"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.chatmanager")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sendHelp(sender, label);
            } else if (args[0].equalsIgnoreCase("delay")) {
                if (args.length > 1) {
                    try {
                        double delay = Double.parseDouble(args[1]);
                        KurisuCore.getChatManager().setDelay(delay < 0 ? 0 : delay);
                        if (delay == 0) {
                            ConfigUtils.broadcast("chat-delay-reset", null);
                        } else {
                            ConfigUtils.broadcast("chat-delay-set", null, new HashMap<String, String>() {{
                                put("{delay}", String.valueOf(delay));
                            }});
                        }
                    } catch (NumberFormatException e) {
                        ConfigUtils.sendMessage(sender, "invalid-number");
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}", CC.formatPlaceholders("/" + label + " delay <delay>", "&c", "&4"));
                    }});
                }
            } else if (args[0].equalsIgnoreCase("mute")) {
                KurisuCore.getChatManager().setMuted(true);
                ConfigUtils.broadcast("chat-muted", null);
            } else if (args[0].equalsIgnoreCase("unmute")) {
                KurisuCore.getChatManager().setMuted(false);
                ConfigUtils.broadcast("chat-unmuted", null);
            } else if (args[0].equalsIgnoreCase("clearchat")) {
                KurisuCore.getChatManager().clear();
                ConfigUtils.broadcast("chat-cleared", null);
            }
        } else {
            ConfigUtils.sendMessage(sender, "no-permission");
        }
        return true;
    }

    private void sendHelp(CommandSender sender, String label) {
        List<String> lines = new ArrayList<>();
        LineUtils.addHeader(lines, sender);
        lines.add("&bChat Manager Help:");
        lines.add(" /{label} delay <delay>");
        lines.add(" /{label} mute");
        lines.add(" /{label} unmute");
        lines.add(" /{label} clearchat");
        LineUtils.addHeader(lines, sender);
        for (String line : lines) {
            sender.sendMessage(CC.color(CC.formatPlaceholders(line.replace("{label}", label), "&f", "&7")));
        }
    }

}