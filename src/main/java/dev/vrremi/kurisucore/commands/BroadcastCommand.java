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
    }
}
