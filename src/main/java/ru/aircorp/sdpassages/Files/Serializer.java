package ru.aircorp.sdpassages.Files;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.aircorp.sdpassages.Client;
import ru.aircorp.sdpassages.SDPassages;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Serializer {
    public static void SerializeClient(Client client, File filePath){
        try (FileWriter writer = new FileWriter(filePath)) {
            YamlConfig yamlConfig = new YamlConfig();
            yamlConfig.writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);

            YamlWriter yamlWriter = new YamlWriter(writer, yamlConfig);
            ClientStruct clientStruct = ConvertClientToStruct(client);

            yamlWriter.write(clientStruct);
            yamlWriter.close();
        }
        catch (IOException e) {
            Bukkit.getLogger().warning("Ошибка сохранения данных клиента в файл: " + filePath.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public static Client DeserializeClient(File filePath){
        try (FileReader fileReader = new FileReader(filePath)) {
            YamlReader yamlReader = new YamlReader(fileReader);
            ClientStruct clientStruct = yamlReader.read(ClientStruct.class);
            String clientName = filePath.toPath().getFileName().toString().replaceFirst("[.][^.]+$", "");

            if (clientStruct != null) {
                return ConvertStructToClient(clientStruct, clientName);
            }
            else {
                Bukkit.getLogger().warning("Ошибка чтение данных клиента из файла: " + filePath.getAbsolutePath());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public static ConfigurationStruct DeserializeConfiguration(File filePath){
        try (FileReader fileReader = new FileReader(filePath)) {
            YamlReader yamlReader = new YamlReader(fileReader);
            ConfigurationStruct configurationStruct = yamlReader.read(ConfigurationStruct.class);

            if (configurationStruct != null) {
                return configurationStruct;
            }
            else {
                Bukkit.getLogger().warning("Ошибка чтение данных клиента из файла: " + filePath.getAbsolutePath());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @NotNull
    private static ClientStruct ConvertClientToStruct(Client client){
        ClientStruct clientStruct = new ClientStruct();
        clientStruct.IsBlocked = client.IsBlocked();
        clientStruct.IsUnlimited = client.IsUnlimited;
        clientStruct.RemainingTime = client.GetRemainingTime();

        return clientStruct;
    }

    @NotNull
    private static Client ConvertStructToClient(ClientStruct clientStruct, String clientName){
        Client client = new Client(clientName, clientStruct.RemainingTime);
        client.IsUnlimited = clientStruct.IsUnlimited;

        return client;
    }
}
