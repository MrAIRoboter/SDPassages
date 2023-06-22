package ru.aircorp.sdpassages.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.aircorp.sdpassages.Client;
import ru.aircorp.sdpassages.SDPassages;

import static org.bukkit.Bukkit.getServer;

public class PlayerQuitEventListener implements Listener {
    private SDPassages _plugin;

    public PlayerQuitEventListener(SDPassages plugin){
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Client client = _plugin.FindOnlineClientByName(event.getPlayer().getName());

        if(client != null)
            _plugin.UnloadClient(client);
    }
}