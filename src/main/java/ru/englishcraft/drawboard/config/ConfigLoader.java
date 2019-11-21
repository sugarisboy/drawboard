package ru.englishcraft.drawboard.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.bukkit.Bukkit;
import ru.englishcraft.drawboard.DrawBoard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class ConfigLoader {

    public Config load() {

        File dir = Bukkit.getPluginManager().getPlugin("DrawBoard").getDataFolder();
        String path = dir.getPath();

        if (!dir.exists())
            dir.mkdir();

        File store = new File(path + "/config.json");
        if (!store.exists()) {
            try {
                Files.copy(
                    DrawBoard.getInstance().getResource("config.json"),
                    store.toPath(),

                    StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                Bukkit.getLogger().warning("File create exception.");
                e.printStackTrace();
            }
        }

        String json = "";
        try {
            Scanner scan = new Scanner(store);
            while (scan.hasNextLine()) {
                json += scan.nextLine() + "\n";
            }
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("Error, file not found!");
        }

        Config config = null;
        try {
            config = gson().fromJson(json, Config.class);
            config.setFile(store);
        } catch (JsonSyntaxException e) {
            Bukkit.getLogger().warning("Config file parse exception:\n" + e.getMessage());
        }

        return config;
    }

    public static Gson gson() {
        return new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }
}
