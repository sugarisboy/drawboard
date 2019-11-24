package ru.englishcraft.drawboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.englishcraft.drawboard.cmd.BoardCommand;
import ru.englishcraft.drawboard.cmd.DrawCommand;
import ru.englishcraft.drawboard.config.Config;
import ru.englishcraft.drawboard.config.ConfigLoader;
import ru.englishcraft.drawboard.listener.MainListener;

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

        getServer().getPluginManager().registerEvents(new MainListener(), this);
        Bukkit.getPluginCommand("board").setExecutor(new BoardCommand());
        Bukkit.getPluginCommand("draw").setExecutor(new DrawCommand());
    }

    public static DrawBoard getInstance() {
        return instance;
    }

    public Config config() {
        return config;
    }
}
