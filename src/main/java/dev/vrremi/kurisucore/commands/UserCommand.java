package dev.vrremi.kurisucore.commands;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Permission;
import dev.vrremi.kurisucore.data.Rank;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.managers.ConnectionPoolManager;
import dev.vrremi.kurisucore.managers.data.UserDataManager;
import dev.vrremi.kurisucore.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserCommand {

    public UserCommand() {
        super("user", "User command", "/user", Collections.singletonList("user"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.user")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sendHelp(sender, label);
            } else if (args[0].equalsIgnoreCase("info")) {
                if (args.length > 1) {
                    Player target = Server.getOnline(args[1]);
                    if (target != null) {
                        User user = KurisuCore.getUserManager().getUser(target);
                        if (user != null) {
                            List<String> lines = new ArrayList<>();
                            LineUtils.addHeader(lines, sender);
                            lines.add("&bUser Information &7(&b" + target.getName() + "&7)");
                            StringBuilder ranks = new StringBuilder();
                            for (Rank rank : user.getRanks()) {
                                ranks.append("&b").append(rank.getName());
                                if (user.getRankExpireTime(rank) != Long.MAX_VALUE) {
                                    ranks.append("&7 (expires in: &b")
                                            .append(Time.millisToTime(user.getRankExpireTime(rank)))
                                            .append("&7)");
