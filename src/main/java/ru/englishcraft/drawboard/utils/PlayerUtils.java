package ru.englishcraft.drawboard.utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import ru.englishcraft.drawboard.board.DrawColor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerUtils {

    public static Vector toDirection(double yaw) {
        Vector v = new Vector(0, 0, 0);

        while (yaw < -180)
            yaw += 360;
        while (yaw > 180)
            yaw -= 360;

        if (-135 < yaw && yaw < -45)
            v.setX(1);
        else if (-45 < yaw && yaw < 45)
            v.setZ(1);
        else if (45 < yaw && yaw < 135)
            v.setX(-1);
        else if (yaw < -135 || yaw > 135)
            v.setZ(-1);
        return v;
    }

    public static ItemStack generateItem(Material material, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);

        if (lore != null) {
            List<String> colorLore = lore.stream()
                .map(line -> " " + ChatColor.GRAY + line + " ")
                .collect(Collectors.toList());
            meta.setLore(colorLore);
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static Inventory convertColorToInv() {
        int length = DrawColor.values().length;
        int size = length / 9 + 1;
        Inventory inventory = Bukkit.createInventory(null, size * 9, ChatColor.DARK_GRAY + "Выберите цвет: ");

        Stream.of(DrawColor.values())
            .filter(DrawColor::isDisplay)
            .map(color -> generateItem(color.getMaterial(), color.getDisplayName(), null))
            .forEach(inventory::addItem);

        return inventory;
    }
}
