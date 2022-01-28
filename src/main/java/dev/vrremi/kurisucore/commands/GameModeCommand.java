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

public class GameModeCommand extends Command{

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
                    }
                }
            }
        }
    }
}
