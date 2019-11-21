package ru.englishcraft.drawboard.config.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;

public class LocationAdapter extends TypeAdapter<Location> {

    @Override
    public void write(JsonWriter out, Location location) throws IOException {
        if (location != null) {
            out.value(String.format(
                "%s %d %d %d",
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
            ));
        } else {
            out.nullValue();
        }
    }

    @Override
    public Location read(JsonReader in) throws IOException {
        String string = in.nextString();
        String[] data = string.split(" ");
        if  (data.length == 4) {
            String worldName = data[0];
            int x = Integer.valueOf(data[1]);
            int y = Integer.valueOf(data[2]);
            int z = Integer.valueOf(data[3]);
            return new Location(Bukkit.getWorld(worldName), x, y, z);
        } else {
            return null;
        }
    }
}
