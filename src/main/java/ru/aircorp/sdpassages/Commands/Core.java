package ru.aircorp.sdpassages.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.aircorp.sdpassages.Files.Permissions;
import ru.aircorp.sdpassages.SDPassages;

import java.util.ArrayList;
import java.util.List;

public class Core implements CommandExecutor, TabCompleter {
    private AddTimeCommand _addTimeCommand;
    private MyTimeCommand _myTimeCommand;
    private RemainingTimeCommand _remainingTimeCommand;

    public Core(){
        _addTimeCommand = new AddTimeCommand(SDPassages.Instance);
        _myTimeCommand = new MyTimeCommand(SDPassages.Instance);
        _remainingTimeCommand = new RemainingTimeCommand(SDPassages.Instance);

        SDPassages.Instance.getCommand("pass").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Создано MrAIRobot");
            commandSender.sendMessage(ChatColor.DARK_GREEN + "----------------------");
            commandSender.sendMessage(ChatColor.GREEN + "Комманды " + ChatColor.DARK_GRAY + ">");
            commandSender.sendMessage(ChatColor.GREEN + "/pass AddTime <Игрок> <Время>");
            commandSender.sendMessage(ChatColor.GREEN + "/pass MyTime");
            commandSender.sendMessage(ChatColor.GREEN + "/pass RemainingTime <Игрок>");
            commandSender.sendMessage(ChatColor.DARK_GREEN + "----------------------");
            return true;
        }

        if(_addTimeCommand.onCommand(commandSender, command, label, args) == true){
            _addTimeCommand.executeCommand(commandSender, command, args);
            return true;
        }

        if(_myTimeCommand.onCommand(commandSender, command, label, args) == true) {
            _myTimeCommand.executeCommand(commandSender, command, args);
            return true;
        }

        if(_remainingTimeCommand.onCommand(commandSender, command, label, args) == true){
            _myTimeCommand.executeCommand(commandSender, command, args);
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        completions.addAll(_addTimeCommand.GetTabCompletions(sender, command, label, args));
        completions.addAll(_myTimeCommand.GetTabCompletions(sender, command, label, args));
        completions.addAll(_remainingTimeCommand.GetTabCompletions(sender, command, label, args));

        return completions;
    }
}
