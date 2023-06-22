package ru.aircorp.sdpassages.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.aircorp.sdpassages.Files.Permissions;
import ru.aircorp.sdpassages.SDPassages;

import java.util.ArrayList;
import java.util.List;

public abstract class PassagesCommand {
    protected SDPassages _plugin;

    public PassagesCommand(final SDPassages plugin){
        _plugin = plugin;
    }

    public abstract void executeCommand(final CommandSender commandSender, final Command command, final String[] args);

    public abstract boolean onCommand(CommandSender commandSender, Command command, String label, String[] args);

    public abstract List<String> GetTabCompletions(CommandSender sender, Command command, String label, String[] args);

    protected List<String> GetAllOnlinePlayersNames(){
        List<String> playerNames = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers())
            playerNames.add(player.getName());

        return playerNames;
    }
}