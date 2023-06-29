package ru.aircorp.sdpassages;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import ru.aircorp.sdpassages.Files.TimeFormatStruct;
import ru.aircorp.sdpassages.Files.Utils;

public class PassagesPlaceholderExpansion extends PlaceholderExpansion {
    private SDPassages _plugin;

    public PassagesPlaceholderExpansion(final SDPassages plugin){
        _plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "pass"; // Идентификатор плейсхолдера
    }

    @Override
    public String getAuthor() {
        return "SDPassages";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    // Значение плейсхолдера для каждого игрока
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("MyTime")) {
            String result = "Нет в системе!";
            Client client = _plugin.FindOnlineClientByName(player.getName());

            if(client != null){
                if(client.IsUnlimited == true)
                    result = "∞";
                else
                    result = Utils.GetFormattedTime(client.GetRemainingTime(), _plugin.GetTimeFormat());
            }

            return result;
        }

        return null;
    }
}