package ru.aircorp.sdpassages.Files;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Utils {
    public static String GetFormattedTime(int seconds, TimeFormatStruct timeFormat) {
        int absSeconds = Math.abs(seconds);
        int days = absSeconds / (60 * 60 * 24);
        int hours = (absSeconds % (60 * 60 * 24)) / (60 * 60);
        int minutes = (absSeconds % (60 * 60)) / 60;
        int remainingSeconds = absSeconds % 60;

        StringBuilder builder = new StringBuilder();

        if(timeFormat.DaysFormat != null)
            if (days > 0)
                builder.append(String.format(timeFormat.DaysFormat, days));

        if(timeFormat.HoursFormat != null)
            if (days > 0 || hours > 0)
                builder.append(String.format(timeFormat.HoursFormat, hours));

        if(timeFormat.MinutesFormat != null)
            if (days > 0 || hours > 0 || minutes > 0)
                builder.append(String.format(timeFormat.MinutesFormat, minutes));

        if(timeFormat.SecondsFormat != null)
            builder.append(String.format(timeFormat.SecondsFormat, remainingSeconds));

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

    public static boolean IsSubcommand(String[] args, String subcommandName, int index){
        if(index > args.length - 1)
            return false;

        return args[index].equalsIgnoreCase(subcommandName) == true;
    }
}
