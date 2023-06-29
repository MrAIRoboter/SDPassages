package ru.aircorp.sdpassages;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.aircorp.sdpassages.Commands.Core;
import ru.aircorp.sdpassages.Files.ConfigurationStruct;
import ru.aircorp.sdpassages.Files.Permissions;
import ru.aircorp.sdpassages.Files.Serializer;
import ru.aircorp.sdpassages.Files.TimeFormatStruct;
import ru.aircorp.sdpassages.Listeners.PlayerLoginEventListener;
import ru.aircorp.sdpassages.Listeners.PlayerQuitEventListener;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;

public final class SDPassages extends JavaPlugin {
    public static SDPassages Instance;

    private ArrayList<Client> _clients;
    private int _countdownTaskId;
    private ConfigurationStruct _configuration;

    @Override
    public void onEnable() {
        Instance = this;
        _clients = new ArrayList<Client>();

        SaveResources(false);
        LoadResources();
        InitializeOnlinePlayers();
        RegisterListeners();
        RegisterCommands();
        RegisterPlaceholders();
        StartCountdown();

        this.getLogger().info("SDPassages включён!");
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTask(_countdownTaskId);
        UnloadClients();

        this.getLogger().info("SDPassages отключён!");
    }

    public Client LoadClient(String playerName){
        Client onlineClient = FindOnlineClientByName(playerName);

        if(onlineClient != null)
            return onlineClient;

        File pluginFolder = getDataFolder();
        File clientsFolder = new File(pluginFolder, "Clients");
        File clientFile = new File(clientsFolder, playerName + ".yml");

        if(clientsFolder.exists() == false)
            clientsFolder.mkdir();

        if(clientFile.exists() == false)
            return RegisterNewClient(playerName);

        Client loadedClient = LoadClient(clientFile);

        if(loadedClient != null){
            return loadedClient;
        }
        else{
            clientFile.delete();

            return RegisterNewClient(playerName);
        }
    }

    public void KickClient(Client client){
        String message = "Вы потратили доступное время, купите ещё в магазине!";
        Player player = Bukkit.getPlayer(client.GetName());
        Component kickReason = Component.text(message);

        UnloadClient(client);
        player.kick(kickReason);
    }

    public void UnloadClient(Client client){
        if(_clients.contains(client) == false)
            return;

        File clientsFolder = new File(getDataFolder(), "Clients");

        if (!clientsFolder.exists()) {
            boolean created = clientsFolder.mkdirs();

            if (!created) {
                getLogger().warning("Не удалось создать папку 'Clients': " + clientsFolder.getAbsolutePath());
                return;
            }
        }

        String fileName = client.GetName() + ".yml";
        File clientFile = new File(clientsFolder, fileName);

        Serializer.SerializeClient(client, clientFile);
        _clients.remove(client);
    }

    @Nullable
    public Client FindOnlineClientByName(String clientName) {
        return _clients.stream()
                .filter(client -> client.GetName().equals(clientName))
                .findFirst()
                .orElse(null);
    }

    public boolean IsPlayerOnline(String playerName) {
        Player player = Bukkit.getPlayer(playerName);

        return player != null && player.isOnline();
    }

    public TimeFormatStruct GetTimeFormat(){
        return _configuration.TimeFormat;
    }

    private void StartCountdown(){
        _countdownTaskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            ProcessCountdownCycle();
        }, 20L, 20L); //20 тиков = 1 секунда
    }

    private void ProcessCountdownCycle(){
        boolean isCycleCompleted = false;

        while (isCycleCompleted == false){
            long clientsCount = _clients.stream().count();
            isCycleCompleted = GetOnlinePlayersCount() == 0 || clientsCount == 0;

            if(isCycleCompleted == true)
                break;

            for (int i = 0; i < clientsCount; i++) {
                Client client = _clients.get(i);
                Player player = Bukkit.getPlayer(client.GetName());

                client.IsUnlimited = Permissions.IsUnlimited(player);

                if(client.IsUnlimited == false){
                    if(_configuration.Notification.IsEnabled == true)
                        if(client.GetRemainingTime() == _configuration.Notification.RemainingTime)
                            player.sendMessage(_configuration.Notification.Message);

                    client.RemoveRemainingTime(1);
                }

                if(client.IsBlocked() == true){
                    KickClient(client);
                    break;
                }

                if(i == clientsCount - 1)
                    isCycleCompleted = true;
            }
        }
    }

    private long GetOnlinePlayersCount(){
         return Bukkit.getOnlinePlayers().stream().count();
    }

    private Client RegisterNewClient(String clientName){
        Client newClient = new Client(clientName, _configuration.BeginnersRemainingTime);

        _clients.add(newClient);
        return newClient;
    }

    private Client LoadClient(File clientFile){
        Client client = Serializer.DeserializeClient(clientFile);

        _clients.add(client);

        return client;
    }

    private void UnloadClients(){
        for (Client client : _clients)
            UnloadClient(client);
    }

    private void RegisterCommands(){
        new Core();
    }

    private void RegisterListeners(){
        PluginManager  pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerLoginEventListener(this), this);
        pluginManager.registerEvents(new PlayerQuitEventListener(this), this);
    }

    private void RegisterPlaceholders(){
        PassagesPlaceholderExpansion passagesPlaceholderExpansion = new PassagesPlaceholderExpansion(this);
        passagesPlaceholderExpansion.register();
    }

    private void SaveResources(boolean replace){
        saveResource("config.yml", replace);
    }

    private void LoadResources(){
        File pluginFolder = getDataFolder();
        File configPath = new File(pluginFolder, "config.yml");

        _configuration = Serializer.DeserializeConfiguration(configPath);
    }

    private void InitializeOnlinePlayers(){
        for(Player player : Bukkit.getOnlinePlayers())
            LoadClient(player.getName());
    }
}