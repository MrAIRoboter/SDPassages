package ru.aircorp.sdpassages.Files;

import org.bukkit.entity.Player;

public class Permissions {
    public static boolean IsAdmin(Player player){
        return player.hasPermission("sdpassages.admin") || player.hasPermission("sdpassages.*");
    }

    public static boolean IsUnlimited(Player player){
        return player.hasPermission("sdpassages.unlimited") || IsAdmin(player) == true;
    }
}