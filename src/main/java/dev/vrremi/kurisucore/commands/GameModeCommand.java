package dev.vrremi.kurisucore.commands;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.utils.CC;
import dev.vrremi.kurisucore.utils.ConfigUtils;
import dev.vrremi.kurisucore.utils.Server;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GameModeCommand extends Command {

    public GameModeCommand() {
        super("gamemode", "Gamemode command", "/gamemode", Arrays.asList("gamemode", "gmc", "gms", "gmsp", "gma"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.gamemode")) {
            if (label.equalsIgnoreCase("gamemode")) {
                if (args.length > 0) {
                    if (Arrays.stream(GameMode.values()).anyMatch(gameMode -> gameMode.toString().equalsIgnoreCase(args[0]))) {
                        GameMode gameMode = GameMode.valueOf(args[0].toUpperCase());
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            KurisuCore.getGameModeManager().setGameMode(player, gameMode);
                            ConfigUtils.sendMessage(sender, "set-gamemode", new HashMap<String, String>() {{
                                put("{player}", player.getName());
                                put("{gamemode}", gameMode.toString().toLowerCase());
                            }});
                        } else {
                            if (args.length > 1) {
                                Player target = Server.getOnline(args[1]);
                                if (target != null) {
                                    KurisuCore.getGameModeManager().setGameMode(target, gameMode);
                                    ConfigUtils.sendMessage(sender, "set-gamemode", new HashMap<String, String>() {{
                                        put("{player}", target.getName());
                                        put("{gamemode}", gameMode.toString().toLowerCase());
                                    }});
                                } else {
                                    ConfigUtils.sendMessage(sender, "player-not-online", new HashMap<String, String>() {{
                                        put("{player}", args[1]);
                                    }});
                                }
                            } else {
                                ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                                    put("{usage}", CC.formatPlaceholders("/" + label + " <gamemode> <player>", "&c", "&4"));
                                }});
                            }
                        }
                    } else {
                        ConfigUtils.sendMessage(sender, "invalid-gamemode");
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}",
                                CC.formatPlaceholders("/" + label + " <gamemode> " + (sender instanceof Player ?
                                                "[player]" : "<player>"),
                                        "&c", "&4"));
                    }});
                }
            } else {
                GameMode gameMode = null;
                switch (label.toLowerCase()) {
                    case "gmc":
                        gameMode = GameMode.CREATIVE;
                        break;
                    case "gms":
                        gameMode = GameMode.SURVIVAL;
                        break;
                    case "gmsp":
                        gameMode = GameMode.SPECTATOR;
                        break;
                    case "gma":
                        gameMode = GameMode.ADVENTURE;
                        break;
                }
                if (gameMode != null) {
                    GameMode finalGameMode = gameMode;
                    if (args.length > 0) {
                        Player target = Server.getOnline(args[0]);
                        if (target != null) {
                            KurisuCore.getGameModeManager().setGameMode(target, gameMode);
                            ConfigUtils.sendMessage(sender, "set-gamemode", new HashMap<String, String>() {{
                                put("{player}", target.getName());
                                put("{gamemode}", finalGameMode.toString().toLowerCase());
                            }});
                        } else {
                            ConfigUtils.sendMessage(sender, "player-not-online", new HashMap<String, String>() {{
                                put("{player}", args[0]);
                            }});
                        }
                    } else {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            KurisuCore.getGameModeManager().setGameMode(player, gameMode);
                }
            }
        } else {
            ConfigUtils.sendMessage(sender, "no-permission");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) throws IllegalArgumentException {
        if (label.equalsIgnoreCase("gamemode")) {
            if (args.length == 1) {
                return Arrays.stream(GameMode.values()).map(Enum::toString).map(String::toLowerCase).collect(Collectors.toList());
            } else if (args.length == 2) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            }
        } else {
            if (args.length == 1) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

}