package ru.aircorp.sdpassages.Listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.Debug;
import ru.aircorp.sdpassages.Client;
import ru.aircorp.sdpassages.Files.Permissions;
import ru.aircorp.sdpassages.SDPassages;

public class PlayerLoginEventListener implements Listener {
    private SDPassages _plugin;

    public PlayerLoginEventListener(SDPassages plugin){
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event){
        Player player = event.getPlayer();
        PlayerLoginEvent.Result loginResult = event.getResult();

        if(loginResult != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        String playerName = player.getName();
        Client client = _plugin.LoadClient(playerName);

        if(Permissions.IsUnlimited(player) == true)
            client.IsUnlimited = true;
        else
            client.IsUnlimited = false;

        if(client.IsBlocked() == true && client.IsUnlimited == false){
            String message = "Вы потратили доступное время, купите ещё в магазине!";
            Component kickReason = Component.text(message);

            _plugin.UnloadClient(client);
            event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
            event.kickMessage(kickReason);
        }
    }
}
