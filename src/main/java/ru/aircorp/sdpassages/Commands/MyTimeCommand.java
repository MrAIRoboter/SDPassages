package ru.aircorp.sdpassages.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.aircorp.sdpassages.Client;
import ru.aircorp.sdpassages.Files.Permissions;
import ru.aircorp.sdpassages.Files.Utils;
import ru.aircorp.sdpassages.SDPassages;

import java.util.ArrayList;
import java.util.List;

public class MyTimeCommand extends PassagesCommand{
    public MyTimeCommand(final SDPassages plugin){
        super(plugin);
    }

    @Override
    public void executeCommand(CommandSender commandSender, Command command, String[] args) {
        //args: MyTime

        if(args.length != 1){
            commandSender.sendMessage("Некорректное использование!");
            return;
        }

        if(commandSender instanceof ConsoleCommandSender){
            commandSender.sendMessage("Данная команда доступа только в игре!");
        }
        else if (commandSender instanceof Player) {
            Player senderPlayer = (Player)commandSender;
            Client senderClient = _plugin.FindOnlineClientByName(senderPlayer.getName());

            if(senderClient != null){
                if(senderClient.IsUnlimited == true){
                    commandSender.sendMessage("Оставшееся у вас время безлимитно!");
                    return;
                }

                int remainingTime = senderClient.GetRemainingTime();

                commandSender.sendMessage("Оставшееся у вас время: " + Utils.GetFormattedTime(remainingTime, _plugin.GetTimeFormat()));
            }
            else{
                commandSender.sendMessage("Вас нет в системе!");
            }
        }
    }

    @Override
    public List<String> GetTabCompletions(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<String>();

        if (args.length == 1)
            completions.add("mytime");

        return completions;
    }

    @Override
    public boolean IsSubcommandBelong(String[] args){
        return Utils.IsSubcommand(args,"mytime", 0);
    }
}