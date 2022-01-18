package dev.vrremi.kurisucore.managers;

import dev.vrremi.kurisucore.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class CommandManager {

    public CommandManager() {
        registerCommands();
    }

    public void registerCommands() {
        registerCommand(new TagCommand());
        registerCommand(new BanCommand());
        registerCommand(new MuteCommand());
        registerCommand(new KickCommand());
        registerCommand(new WarnCommand());
        registerCommand(new GrantCommand());
        registerCommand(new RankCommand());
        registerCommand(new UserCommand());
        registerCommand(new HistoryCommand());
        registerCommand(new GameModeCommand());
        registerCommand(new MessageCommand());
        registerCommand(new ReplyCommand());
        registerCommand(new BroadcastCommand());
        registerCommand(new ChatManagerCommand());
        registerCommand(new StaffChatCommand());

    }

    public void registerCommand(Command command) {
        final Field bukkitCommandMap;
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(command.getName(), command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
