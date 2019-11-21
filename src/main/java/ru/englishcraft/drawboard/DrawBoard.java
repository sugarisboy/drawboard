package ru.englishcraft.drawboard;

import org.bukkit.plugin.java.JavaPlugin;
import ru.englishcraft.drawboard.config.Config;
import ru.englishcraft.drawboard.config.ConfigLoader;

public class DrawBoard extends JavaPlugin {

    private static DrawBoard instance;

    private Config config;

    public DrawBoard() {
        instance = this;
    }

    @Override
    public void onEnable() {
        ConfigLoader configLoader = new ConfigLoader();
        this.config = configLoader.load();
    }

    public static DrawBoard getInstance() {
        return instance;
    }

    public Config config() {
        return config;
    }
}
