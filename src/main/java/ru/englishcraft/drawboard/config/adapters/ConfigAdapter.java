package ru.englishcraft.drawboard.config.adapters;


import ru.englishcraft.drawboard.config.ConfigLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigAdapter {

    private transient File file;

    public void save() {
        if (file.canWrite()) {
            try {
                try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
                    writer.write(asStringContent());
                }
            } catch (IOException e) {
                System.out.println("Ошибка записи изменений.");
                e.printStackTrace();
            }
        }
    }

    public File getFile() {
        return file;
    }

    public String asStringContent() {
        return ConfigLoader.gson().toJson(this);
    }

    public void setFile(File file) {
        this.file = file;
    }
}
