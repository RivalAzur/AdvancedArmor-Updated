package me.ilucah.advancedarmor.armor;

import org.bukkit.Color;
import org.bukkit.plugin.Plugin;

public class ArmorColor {

    private String name;
    private int r;
    private int g;
    private int b;

    public static ArmorColor valueOf(Plugin plugin, String path) {
        int r = plugin.getConfig().getInt("ArmorColor." + path + ".R");
        int g = plugin.getConfig().getInt("ArmorColor." + path + ".G");
        int b = plugin.getConfig().getInt("ArmorColor." + path + ".B");
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

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

}
