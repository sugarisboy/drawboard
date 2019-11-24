package ru.englishcraft.drawboard.board;

import lombok.Getter;
import org.bukkit.Material;

public enum DrawColor {

    WHITE(Material.WHITE_WOOL, "Белый", "白色"),
    ORANGE(Material.ORANGE_WOOL,"Оранжевый" , "橘色"),
    MAGENTA(Material.MAGENTA_WOOL, "Пурпурный", "紫色"),
    LIGHT_BLUE(Material.LIGHT_BLUE_WOOL, "Голубой", "蓝色"),
    YELLOW(Material.YELLOW_WOOL, "Желтый", "黄色"),
    GREEN(Material.LIME_WOOL, "Зеленый", "绿色"),
    PINK(Material.PINK_WOOL, "Розовый", "粉红色"),
    GRAY(Material.GRAY_WOOL, "Серый", "灰色"),
    LIGHT_GRAY(Material.LIGHT_GRAY_WOOL, "Светло-серый", "灰色"),
    CYAN(Material.CYAN_WOOL, "Бирюзовый", "绿松石"),
    PURPLE(Material.PURPLE_WOOL,"Фиолетовый" , "紫色"),
    BLUE(Material.BLUE_WOOL, "Синий", "蓝色"),
    BROWN(Material.BROWN_WOOL, "Коричневый", "棕色"),
    DARK_GREEN(Material.GREEN_WOOL, "Темно-зеленый", "绿色"),
    RED(Material.RED_WOOL, "Красный", "红色"),
    BLACK(Material.BLACK_WOOL, "Черный", "黑色"),
    AIR(Material.AIR, "Unnamed", "Unnamed", false);

    private static final boolean USE_CHINE_NAME = true;

    @Getter
    private Material material;
    @Getter
    private boolean display;

    private String russian;
    private String chinese;

    DrawColor(Material material, String russian, String chinese) {
        this.material = material;
        this.russian = russian;
        this.chinese = chinese;
        this.display = true;
    }

    DrawColor(Material material, String russian, String chinese, boolean display) {
        this.material = material;
        this.russian = russian;
        this.chinese = chinese;
        this.display = display;
    }

    public String getDisplayName() {
        return USE_CHINE_NAME ? chinese : russian;
    }

    public static DrawColor valueOf(Material material) {
        for (DrawColor drawColor : values()) {
            if (drawColor.getMaterial().equals(material)) return drawColor;
        }
        return null;
    }
}
