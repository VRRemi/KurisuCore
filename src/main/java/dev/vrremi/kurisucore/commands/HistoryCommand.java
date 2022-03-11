package dev.vrremi.kurisucore.commands;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Punishment;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.managers.ConnectionPoolManager;
import dev.vrremi.kurisucore.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryCommand extends Command {

    public HistoryCommand() {
        super("history", "History command", "/history", Collections.singletonList("history"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.history")) {
            if (args.length > 0) {
                Player target = Server.getOnline(args[0]);
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("clear")) {
                        if (target != null) {
                            User user = KurisuCore.getUserManager().getUser(target);
                            if (user != null) {
                                user.clearPunishments();
                                ConfigUtils.sendMessage(sender, "history-cleared", new HashMap<String, String>() {{
                                    put("{player}", target.getName());
                                }});
                            } else {
                                ConfigUtils.sendMessage(sender, "still-loading", new HashMap<String, String>() {{
                                    put("{player}", target.getName());
                                }});
                            }
                        } else {
                            Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
                            try {
                                if (KurisuCore.getUserDataManager().playerExists(args[0], connection)) {
                                    UUID uuid = KurisuCore.getUserDataManager().getUUID(args[0], connection);
                                    KurisuCore.getUserDataManager().setPunishments(uuid, new ArrayList<>(), connection);
                                    String realName = KurisuCore.getUserDataManager().getName(uuid, connection);
                                    ConfigUtils.sendMessage(sender, "history-cleared", new HashMap<String, String>() {{
                                        put("{player}", realName);
                                    }});
                                } else {
                                    ConfigUtils.sendMessage(sender, "player-doesnt-exist", new HashMap<String, String>() {{
                                        put("{player}", args[0]);
                                    }});
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } finally {
                                ConnectionPoolManager.close(connection);
                            }
                        }
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        if (args.length > 2) {
                            String id = args[2].replace("#", "");
                            if (target != null) {
                                User user = KurisuCore.getUserManager().getUser(target);
                                if (user != null) {
                                    if (user.hasPunishment(id)) {
                                        user.removePunishment(id);
                                        ConfigUtils.sendMessage(sender, "history-removed", new HashMap<String, String>() {{
                                            put("{player}", target.getName());
                                            put("{id}", id);
                                        }});
                                    } else {
                                        ConfigUtils.sendMessage(sender, "history-id-not-found", new HashMap<String, String>() {{
                                            put("{player}", target.getName());
                                            put("{id}", id);
                                        }});
                                    }
                                } else {
                                    ConfigUtils.sendMessage(sender, "still-loading", new HashMap<String, String>() {{
                                        put("{player}", target.getName());
                                    }});
                                }
                            } else {
                                Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
                                try {
                                    if (KurisuCore.getUserDataManager().playerExists(args[0], connection)) {
                                        UUID uuid = KurisuCore.getUserDataManager().getUUID(args[0], connection);
                                        List<Punishment> punishments =
                                                KurisuCore.getUserDataManager().getPunishments(uuid, connection);
                                        Punishment punishment = null;
                                        for (Punishment p : punishments) {
                                            if (p.getId().equalsIgnoreCase(id)) {
                                                punishment = p;
                                            }
                                        }
                                        String realName = KurisuCore.getUserDataManager().getName(uuid, connection);
                                        if (punishment != null) {
                                            punishments.remove(punishment);
                                            KurisuCore.getUserDataManager().setPunishments(uuid, punishments, connection);
                                            ConfigUtils.sendMessage(sender, "history-removed", new HashMap<String, String>() {{
                                                put("{player}", realName);
                                                put("{id}", id);
                                            }});
                                        } else {
                                            ConfigUtils.sendMessage(sender, "history-id-not-found", new HashMap<String, String>() {{
                                                put("{player}", realName);
                                                put("{id}", id);
                                            }});
                                        }
                                    } else {
                                        ConfigUtils.sendMessage(sender, "player-doesnt-exist", new HashMap<String, String>() {{
                                            put("{player}", args[0]);
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
                                put("{usage}", CC.formatPlaceholders("/" + label + " <player> remove <id>", "&c",
                                        "&4"));
                            }});
                        }
                    } else {
                        sendHelp(sender, label);
                    }
                } else {
                    String name = null;
                    List<Punishment> punishments = null;
                    List<String> lines = new ArrayList<>();
                    boolean send = false;
                    if (target != null) {
                        User user = KurisuCore.getUserManager().getUser(target);
                        if (user != null) {
                            name = user.getName();
                            punishments = user.getPunishments();
                            send = true;
                        } else {
                            ConfigUtils.sendMessage(sender, "still-loading", new HashMap<String, String>() {{
                                put("{player}", target.getName());
                            }});
                        }
                    } else {
                        Connection connection = KurisuCore.getConnectionPoolManager().getConnection();
                        try {
                            if (KurisuCore.getUserDataManager().playerExists(args[0], connection)) {
                                UUID uuid = KurisuCore.getUserDataManager().getUUID(args[0], connection);
                                punishments = KurisuCore.getUserDataManager().getPunishments(uuid, connection);
                                name = KurisuCore.getUserDataManager().getName(uuid, connection);
                                send = true;
                            } else {
                                ConfigUtils.sendMessage(sender, "player-doesnt-exist", new HashMap<String, String>() {{
                                    put("{player}", args[0]);
                                }});
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionPoolManager.close(connection);
                        }
                    }
                    if (send && punishments != null) {
                        LineUtils.addHeader(lines, sender);
                        lines.add("&bPunishment History: &7(&b" + name + "&7)");
                        if (punishments.size() == 0) {
                            lines.add("&7* No punishments");
                        } else {
                            for (Punishment punishment : punishments) {
                                lines.add("&7* &b" + punishment.getPunishmentType().readableName() + " &7(&b#" + punishment.getId() + "&7)");
                                String date =
                                        new SimpleDateFormat("h:mm aa").format(new Date(punishment.getCreated())) + "&7, " +
                                                "&b" + new SimpleDateFormat("MM/dd/yy").format(new Date(punishment.getCreated()));
                                lines.add("&8  - &7Date: &b" + date);
                                lines.add("&8  - &7Reason: &b" + (punishment.getReason().isEmpty() ? "No reason " +
                                        "provided" : punishment.getReason()));
                                lines.add("&8  - &7Punisher: &b" + punishment.getPunisher());
                                String expire = punishment.getTimeout() == Long.MAX_VALUE ? "Never" :
                                        Time.millisToTime(punishment.getTimeout());
                                lines.add("&8  - &7Expires: &b" + (expire.equals("0s") ? "expired" : expire));
                            }
                        }
                        LineUtils.addHeader(lines, sender);
                        lines.forEach(line -> sender.sendMessage(CC.color(line)));
                    }
                }
            } else {
                sendHelp(sender, label);
            }
        } else {
            ConfigUtils.sendMessage(sender, "no-permission");
        }
        return true;
    }

    private void sendHelp(CommandSender sender, String label) {
        List<String> lines = new ArrayList<>();
        LineUtils.addHeader(lines, sender);
        lines.add("&bHistory Help:");
        lines.add(" /{label} <player>");
        lines.add(" /{label} <player> clear");
        lines.add(" /{label} <player> remove <id>");
        LineUtils.addHeader(lines, sender);
        lines.stream().map(line -> CC.color(CC.formatPlaceholders(line.replace("{label}", label), "&f", "&7"))).forEach(sender::sendMessage);
    }

}
