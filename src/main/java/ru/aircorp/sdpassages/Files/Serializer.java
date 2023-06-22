package ru.aircorp.sdpassages.Files;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.aircorp.sdpassages.Client;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Serializer {
    public static void SerializeClient(Client client, File filePath){
        try (FileWriter writer = new FileWriter(filePath)) {
            YamlWriter yamlWriter = new YamlWriter(writer);
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

            if (clientStruct != null) {
                return ConvertStructToClient(clientStruct);
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
        clientStruct.Name = client.GetName();
        clientStruct.RemainingTime = client.GetRemainingTime();

        return clientStruct;
    }

    @NotNull
    private static Client ConvertStructToClient(ClientStruct clientStruct){
        Client client = new Client(clientStruct.Name, clientStruct.RemainingTime);
        client.IsUnlimited = clientStruct.IsUnlimited;

        return client;
    }
}
