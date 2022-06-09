package me.ilucah.advancedarmor.armor;

import me.ilucah.advancedarmor.AdvancedArmor;
import org.bukkit.Color;
import org.bukkit.plugin.Plugin;

public class ArmorColor {

    private String name;
    private int r;
    private int g;
    private int b;

    public static ArmorColor valueOf(AdvancedArmor plugin, String path) {
        int r = plugin.getArmor().getInt("ArmorColor." + path + ".R");
        int g = plugin.getArmor().getInt("ArmorColor." + path + ".G");
        int b = plugin.getArmor().getInt("ArmorColor." + path + ".B");
        return new ArmorColor(path, r, g, b);
    }

    public ArmorColor (String name, int r, int g, int b) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color getColor() {
        return Color.fromRGB(r, g, b);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
