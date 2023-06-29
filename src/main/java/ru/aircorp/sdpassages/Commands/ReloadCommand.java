package ru.aircorp.sdpassages.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import ru.aircorp.sdpassages.Files.Utils;
import ru.aircorp.sdpassages.SDPassages;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends PassagesCommand{
    public ReloadCommand(final SDPassages plugin){
        super(plugin);
    }

    @Override
    public void executeCommand(CommandSender commandSender, Command command, String[] args) {
        if(Utils.IsCommandSenderAdmin(commandSender) == false){
            commandSender.sendMessage("У вас недостаточно прав!");
            return;
        }

        if(args.length != 1){
            commandSender.sendMessage("Некорректное использование!");
            return;
        }

        PluginManager pluginManager = _plugin.getServer().getPluginManager();
        pluginManager.disablePlugin(_plugin);
        pluginManager.enablePlugin(_plugin);
        commandSender.sendMessage("Плагин успешно перезагружен!");
    }

    @Override
    public List<String> GetTabCompletions(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<String>();
        boolean isAdmin = Utils.IsCommandSenderAdmin(sender);

        if(isAdmin == true){
            if(args.length == 1)
                completions.add("reload");
        }

        return completions;
    }

    @Override
    public boolean IsSubcommandBelong(String[] args){
        return Utils.IsSubcommand(args,"reload", 0);
    }
}
