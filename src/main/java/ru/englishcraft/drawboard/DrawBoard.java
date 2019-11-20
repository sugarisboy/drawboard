package ru.englishcraft.drawboard;

import org.bukkit.plugin.java.JavaPlugin;

public class DrawBoard extends JavaPlugin {

    private static DrawBoard instance;

    public DrawBoard() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // TODO
    }

    public static DrawBoard getInstance() {
        return instance;
    }
}
