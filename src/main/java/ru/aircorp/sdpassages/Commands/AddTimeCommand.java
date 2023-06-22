package ru.aircorp.sdpassages.Commands;

import org.bukkit.ChatColor;
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

public class AddTimeCommand extends PassagesCommand{
    public AddTimeCommand(final SDPassages plugin){
        super(plugin);
    }

    @Override
    public void executeCommand(CommandSender commandSender, Command command, String[] args) {
        //args: pass AddTime <Игрок> <Время>

        if(args.length != 3){
            commandSender.sendMessage("Некорректное использование!");
            return;
        }

        String playerName = args[1];
        int time = Integer.parseInt(args[2]);

        AddTime(commandSender, playerName, time);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (Utils.IsSubcommand(args,"AddTime") == true) {
            if(Utils.IsCommandSenderAdmin(commandSender) == false){
                commandSender.sendMessage("У вас недостаточно прав!");
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> GetTabCompletions(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<String>();
        boolean isAdmin = Utils.IsCommandSenderAdmin(sender);

        if (args.length == 1) { // Автодополнение для первого аргумента команды /pass
            if (isAdmin == true)
                completions.add("AddTime");
        }
        else if (args.length == 2) { // Автодополнение для второго аргумента команды /pass
            if(isAdmin == true)
                if (args[0].equalsIgnoreCase("AddTime"))
                    completions.add("300");
        }

        return completions;
    }

    private void AddTime(CommandSender commandSender, String playerName, int time){
        if(time <= 0){
            commandSender.sendMessage("Пополняемое время должно быть больше нуля!");
            return;
        }

        Client client = _plugin.LoadClient(playerName);
        client.AddRemainingTime(time);

        if(_plugin.IsPlayerOnline(playerName) == false)
            _plugin.UnloadClient(client);

        commandSender.sendMessage("Время " + time + " с. добавлено игроку " + playerName);
    }
}
