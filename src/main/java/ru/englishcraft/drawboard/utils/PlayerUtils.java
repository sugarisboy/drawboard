package ru.englishcraft.drawboard.utils;


import org.bukkit.util.Vector;

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
}
