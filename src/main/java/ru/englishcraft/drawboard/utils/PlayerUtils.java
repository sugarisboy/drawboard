package ru.englishcraft.drawboard.utils;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerUtils {

    public static Vector toDirection(double yaw) {
        Vector v = new Vector(0, 0, 0);
        if (-135 < yaw && yaw < -45) {
            v.setX(1);
        } else if (-45 < yaw && yaw < 45) {
            v.setZ(1);
        } else if (45 < yaw && yaw < 135) {
            v.setX(-1);
        } else {
            v.setZ(-1);
        }

        return v;
    }

    public static ItemStack generateItem(Material material, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);

        List<String> colorLore = lore.stream().map(line -> " " + ChatColor.GRAY + line + " ").collect(Collectors.toList());
        meta.setLore(colorLore);

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
