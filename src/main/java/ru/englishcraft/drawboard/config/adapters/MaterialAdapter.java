package ru.englishcraft.drawboard.config.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Material;

import java.io.IOException;

public class MaterialAdapter extends TypeAdapter<Material> {

    @Override
    public void write(JsonWriter out, Material material) throws IOException {
        if (material != null) {
            out.value(material.name());
        } else {
            out.nullValue();
        }
    }

    @Override
    public Material read(JsonReader in) throws IOException {
        String value = in.nextString();
        return Material.valueOf(value);
    }
}
