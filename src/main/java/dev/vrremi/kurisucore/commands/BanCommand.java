package dev.vrremi.kurisucore.commands;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.Punishment;
import dev.vrremi.kurisucore.data.PunishmentType;
import dev.vrremi.kurisucore.data.User;
import dev.vrremi.kurisucore.managers.ConnectionPoolManager;
import dev.vrremi.kurisucore.data.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class BanCommand extends Command {

    public BanCommand() {
        super("ban", "Ban command", "/ban", Arrays.asList("ban", "tempban", "tban", "unban"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("kurisu.ban")) {
            String punisher = sender instanceof Player ? sender.getName() : "Console";
            if (label.equalsIgnoreCase("ban")) {
                if (args.length > 0) {
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
                            user.ban(reason, punisher);
                            ConfigUtils.sendMessage(sender, "user-banned", new HashMap<String, String>() {{
                                put("{player}", target.getName());
                                put("{reason}", finalReason.isEmpty() ? "No reason provided" : finalReason);
                            }});
                        } else {
                            ConfigUtils.sendMessage(sender, "error-banning", new HashMap<String, String>() {{
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
                                punishments.add(new Punishment(PunishmentType.BAN, reason, punisher, System.currentTimeMillis(),
                                        Long.MAX_VALUE));
                                KurisuCore.getUserDataManager().setPunishments(uuid, punishments, connection);
                                String realName = KurisuCore.getUserDataManager().getName(uuid, connection);
                                ConfigUtils.sendMessage(sender, "user-banned", new HashMap<String, String>() {{
                                    put("{player}", realName);
                                    put("{reason}", finalReason.isEmpty() ? "No reason provided" : finalReason);
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
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}", CC.formatPlaceholders("/" + label + " <player> [reason]", "&c",
                                "&4"));
                    }});
                }
            } else if (Args.matches(label, "tempban", "tban")) {
                if (args.length > 1) {
                    if (!args[1].isEmpty() && Pattern.compile("(\\d*w)?(\\d*d)?(\\d*h)?(\\d*m)?(\\d*s)?").matcher(args[1]).matches()) {
                        long delay = Time.timeToMillis(args[1]);
                        Player target = Server.getOnline(args[0]);
                        String reason = "";
                        if (args.length > 2) {
                            StringBuilder reasonSB = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                reasonSB.append(args[i]).append(" ");
                            }
                            reason = reasonSB.substring(0, reasonSB.length() - 1);
                        }
                        String finalReason = reason;
                        if (target != null) {
                            User user = KurisuCore.getUserManager().getUser(target);
                            if (user != null) {
                                user.tempBan(reason, punisher, delay);
                                ConfigUtils.sendMessage(sender, "user-temp-banned", new HashMap<String, String>() {{
                                    put("{player}", target.getName());
                                    put("{reason}", finalReason.isEmpty() ? "No reason provided" : finalReason);
                                    put("{time}", Time.millisToTime(delay + System.currentTimeMillis()));
                                }});
                            } else {
                                ConfigUtils.sendMessage(sender, "error-banning", new HashMap<String, String>() {{
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
                                    punishments.add(new Punishment(PunishmentType.TEMP_BAN, reason, punisher,
                                            System.currentTimeMillis(),
                                            System.currentTimeMillis() + delay));
                                    KurisuCore.getUserDataManager().setPunishments(uuid, punishments, connection);
                                    String realName = KurisuCore.getUserDataManager().getName(uuid, connection);
                                    ConfigUtils.sendMessage(sender, "user-temp-banned", new HashMap<String, String>() {{
                                        put("{player}", realName);
                                        put("{reason}", finalReason.isEmpty() ? "No reason provided" : finalReason);
                                        put("{time}", Time.millisToTime(delay + System.currentTimeMillis()));
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
                    } else {
                        ConfigUtils.sendMessage(sender, "invalid-format", new HashMap<String, String>() {{
                            put("{format}", "7d24h60m60s");
                        }});
                    }
                } else {
                    ConfigUtils.sendMessage(sender, "invalid-usage", new HashMap<String, String>() {{
                        put("{usage}", CC.formatPlaceholders("/" + label + " <player> <duration> [reason]", "&c",
                                "&4"));
                    }});
                }
            } else {
