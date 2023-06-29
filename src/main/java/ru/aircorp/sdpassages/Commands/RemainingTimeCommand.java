package ru.aircorp.sdpassages.Commands;

import org.bukkit.Bukkit;
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

public class RemainingTimeCommand extends PassagesCommand{
    public RemainingTimeCommand(final SDPassages plugin){
        super(plugin);
    }

    @Override
    public void executeCommand(CommandSender commandSender, Command command, String[] args) {
        //args: remaining <Игрок>

        if(Utils.IsCommandSenderAdmin(commandSender) == false){
            commandSender.sendMessage("У вас недостаточно прав!");
            return;
        }

        if(args.length != 2){
            commandSender.sendMessage("Некорректное использование!");
            return;
        }

        String playerName = args[1];
        Client client = _plugin.LoadClient(playerName);

        if(client != null){
            commandSender.sendMessage("Оставшееся время у игрока " + playerName +  ": " + Utils.GetFormattedTime(client.GetRemainingTime(), _plugin.GetTimeFormat()));

            if(_plugin.IsPlayerOnline(playerName) == false)
                _plugin.UnloadClient(client);
        }
    }

    @Override
    public List<String> GetTabCompletions(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<String>();
        boolean isAdmin = Utils.IsCommandSenderAdmin(sender);

        if(isAdmin == true){
            if(args.length == 1)
                completions.add("remaining");

            if(Utils.IsSubcommand(args, "remaining", 0) == true){
                switch (args.length){
                    case 2:
                        completions.addAll(GetAllOnlinePlayersNames());
                        break;
                }
            }
        }

        return completions;
    }

    @Override
    public boolean IsSubcommandBelong(String[] args){
        return Utils.IsSubcommand(args,"remaining", 0);
    }
}
