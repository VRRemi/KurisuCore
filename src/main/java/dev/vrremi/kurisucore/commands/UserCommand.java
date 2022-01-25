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
                                }
                                ranks.append("&7, ");
                            }
                            lines.add("&fRanks&7: &b" + ranks.substring(0, ranks.length() - 4));
                            if (user.getActivePermissionCount() > 0) {
                                lines.add(CC.color("&fPermissions&7: (&f" + user.getActivePermissionCount() + "&7)"));
                                StringBuilder permissions = new StringBuilder();
                                for (Permission permission : user.getPermissions()) {
                                    if (!permission.hasTimedOut()) {
                                        permissions
                                                .append("&b")
                                                .append(permission.getNode());
                                        if (permission.getTimeout() != Long.MAX_VALUE) {
                                            permissions.append("&7 (expires in: &b")
                                                    .append(Time.millisToTime(permission.getTimeout()))
                                                    .append("&7)");
                                        }
                                        permissions.append("&7, ");
                                    }
                                }
                                lines.add(CC.color(permissions.substring(0, permissions.length() - 4)));
                            } else {
                                lines.add("&fPermissions&7: &fNone");
                            }
                            LineUtils.addHeader(lines, sender);
                            lines.forEach(line -> sender.sendMessage(CC.color(line)));
                        } else {
                            ConfigUtils.sendMessage(sender, "still-loading", new HashMap<String, String>() {{
                                put("{player}", target.getName());
                            }});
                        }
                    } else {
                        Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
                        try {
                            UserDataManager userDataManager = KurisuCore.getUserDataManager();
                            if (userDataManager.playerExists(args[1], connection)) {
                                UUID uuid = userDataManager.getUUID(args[1], connection);
                                String name = userDataManager.getName(uuid, connection);
                                Rank rank = userDataManager.getRanks(uuid, connection).getHighestRank();
                                List<Permission> permissions =
                                        userDataManager.getPermissions(uuid, connection).stream().filter(Permission::isActive).collect(Collectors.toList());
                                List<String> lines = new ArrayList<>();
                                LineUtils.addHeader(lines, sender);
                                lines.add("&bUser Information &7(&b" + name + "&7)");
                                lines.add("&fRank&7: &b" + (rank == null ? "None" : rank.getName()));
                                if (permissions.size() > 0) {
                                    lines.add(CC.color("&fPermissions&7: (&f" + permissions.size() + "&7)"));
                                    StringBuilder permString = new StringBuilder();
                                    for (Permission permission : permissions) {
                                        if (!permission.hasTimedOut()) {
                                            permString
                                                    .append("&b")
                                                    .append(permission.getNode());
                                            if (permission.getTimeout() != Long.MAX_VALUE) {
                                                permString.append("&7 (expires in: &b")
                                                        .append(Time.millisToTime(permission.getTimeout()))
                                                        .append("&7)");
                                            }
                                            permString.append("&7, ");
                                        }
                                    }
                                    lines.add(CC.color(permString.substring(0, permString.length() - 4)));
                                } else {
                                    lines.add("&fPermissions&7: &fNone");
                                }
                                LineUtils.addHeader(lines, sender);
                                lines.forEach(line -> sender.sendMessage(CC.color(line)));
                            } else {
                                ConfigUtils.sendMessage(sender, "player-doesnt-exist", new HashMap<String, String>() {{
                                    put("{player}", args[1]);
                                }});
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionPoolManager.close(connection);
                        }
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}", CC.formatPlaceholders("/" + label + " info <player>", "&c",
                                "&4"));
                    }});
                }
            } else if (args[0].equalsIgnoreCase("addperm")) {
                if (args.length > 2) {
                    Player target = Server.getOnline(args[1]);
                    if (target != null) {
                        User user = KurisuCore.getUserManager().getUser(target);
                        if (user != null) {
                            if (args.length > 3) {
                                if (Pattern.compile("(\\d*w)?(\\d*d)?(\\d*h)?(\\d*m)?(\\d*s)?").matcher(args[3]).matches()) {
                                    user.addPermission(new Permission(args[2],
                                            System.currentTimeMillis() + Time.timeToMillis(args[3])));
                                    ConfigUtils.sendMessage(sender, "user-timed-permission-added", new HashMap<String, String>() {{
                                        put("{player}", target.getName());
                                        put("{permission}", args[2]);
                                        put("{time}", Time.millisToTime(System.currentTimeMillis() + Time.timeToMillis(args[3])));
                                    }});
                                } else {
                                    ConfigUtils.sendMessage(sender, "invalid-format", new HashMap<String, String>() {{
                                        put("{format}", "7d24h60m60s");
                                    }});
                                }
                            } else {
                                user.addPermission(new Permission(args[2], Long.MAX_VALUE));
                                ConfigUtils.sendMessage(sender, "user-permission-added", new HashMap<String, String>() {{
                                    put("{player}", target.getName());
                                    put("{permission}", args[2]);
                                }});
                            }
                        } else {
                            ConfigUtils.sendMessage(sender, "still-loading", new HashMap<String, String>() {{
                                put("{player}", target.getName());
                            }});
                        }
                    } else {
                        ConfigUtils.sendMessage(sender, "player-not-online", new HashMap<String, String>() {{
                            put("{player}", args[1]);
                        }});
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}", CC.formatPlaceholders("/" + label + " addperm <player> <permission> [timespec]", "&c",
                                "&4"));
                    }});
                }
            } else if (args[0].equalsIgnoreCase("delperm")) {
                if (args.length > 2) {
                    Player target = Server.getOnline(args[1]);
                    if (target != null) {
                        User user = KurisuCore.getUserManager().getUser(target);
                        if (user != null) {
                            user.removePermission(args[2]);
                            ConfigUtils.sendMessage(sender, "user-permission-removed", new HashMap<String, String>() {{
                                put("{player}", target.getName());
                                put("{permission}", args[2]);
                            }});
                        } else {
                            ConfigUtils.sendMessage(sender, "still-loading", new HashMap<String, String>() {{
                                put("{player}", target.getName());
                            }});
                        }
                    } else {
                        ConfigUtils.sendMessage(sender, "player-not-online", new HashMap<String, String>() {{
                            put("{player}", args[1]);
                        }});
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}", CC.formatPlaceholders("/" + label + " delperm <player> <permission>",
                                "&c",
                                "&4"));
                    }});
                }
            } else if (args[0].equalsIgnoreCase("addrankperm")) {
                if (args.length > 2) {
                    Player target = Server.getOnline(args[1]);
                    if (target != null) {
                        Rank rank = KurisuCore.getRankManager().getRank(args[2]);
                        if (rank != null) {
                            User user = KurisuCore.getUserManager().getUser(target);
                            if (user != null) {
                                rank.getPermissions().forEach(user::addPermission);
                                ConfigUtils.sendMessage(sender, "user-rank-permission-added", new HashMap<String, String>() {{
                                    put("{player}", target.getName());
                                    put("{rank}", rank.getName());
                                }});
                            } else {
                                ConfigUtils.sendMessage(sender, "still-loading", new HashMap<String, String>() {{
                                    put("{player}", target.getName());
                                }});
                            }
                        } else {
                            ConfigUtils.sendMessage(sender, "rank-not-found", new HashMap<String, String>() {{
                                put("{rank}", args[2]);
                            }});
                        }
                    } else {
                        ConfigUtils.sendMessage(sender, "player-not-online", new HashMap<String, String>() {{
                            put("{player}", args[1]);
                        }});
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}", CC.formatPlaceholders("/" + label + " addrankperm <player> <rank>",
                                "&c",
                                "&4"));
                    }});
                }
            } else if (args[0].equalsIgnoreCase("delrankperm")) {
                if (args.length > 2) {
                    Player target = Server.getOnline(args[1]);
                    if (target != null) {
                        Rank rank = KurisuCore.getRankManager().getRank(args[2]);
                        if (rank != null) {
                            User user = KurisuCore.getUserManager().getUser(target);
                            if (user != null) {
                                rank.getPermissions().stream().map(Permission::getNode).forEach(user::removePermission);
                                ConfigUtils.sendMessage(sender, "user-rank-permission-removed",
                                        new HashMap<String, String>() {{
                                            put("{player}", target.getName());
                                            put("{rank}", rank.getName());
                                        }});
                            } else {
                                ConfigUtils.sendMessage(sender, "still-loading", new HashMap<String, String>() {{
                                    put("{player}", target.getName());
                                }});
                            }
                        } else {
                            ConfigUtils.sendMessage(sender, "rank-not-found", new HashMap<String, String>() {{
                                put("{rank}", args[2]);
                            }});
                        }
                    } else {
                        ConfigUtils.sendMessage(sender, "player-not-online", new HashMap<String, String>() {{
                            put("{player}", args[1]);
                        }});
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}", CC.formatPlaceholders("/" + label + " delrankperm <player> <rank>",
                                "&c",
                                "&4"));
                    }});
                }
