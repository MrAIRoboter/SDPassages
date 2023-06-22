package ru.aircorp.sdpassages.Files;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class Utils {
    public static String GetFormattedTime(int seconds) {
        int absSeconds = Math.abs(seconds);
        int days = absSeconds / (60 * 60 * 24);
        int hours = (absSeconds % (60 * 60 * 24)) / (60 * 60);
        int minutes = (absSeconds % (60 * 60)) / 60;
        int remainingSeconds = absSeconds % 60;

        StringBuilder builder = new StringBuilder();

        if (days > 0) {
            builder.append(days).append(" д. ");
        }
        if (days > 0 || hours > 0) {
            builder.append(String.format("%d ч. ", hours));
        }
        if (days > 0 || hours > 0 || minutes > 0) {
            builder.append(String.format("%02d м. ", minutes));
        }
        builder.append(String.format("%02d с.", remainingSeconds));

        return builder.toString();
    }

    public static boolean IsCommandSenderAdmin(CommandSender sender){
        boolean isAdmin = false;

        if(sender instanceof ConsoleCommandSender)
            isAdmin = true;
        else if(sender instanceof Player)
            isAdmin = Permissions.IsAdmin(((Player) sender).getPlayer()) == true;

        return isAdmin;
    }

    public static boolean IsSubcommand(String[] args, String subcommandName){
        return args[0].equalsIgnoreCase(subcommandName) == true;
    }
}
