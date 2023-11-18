package me.ilucah.advancedarmor.listener;

import org.bukkit.inventory.*;

public class ShardObj {



    private final String shardTypeName;
    private final String armorTypeName;
    private final int menuSlot;
    private final int helmetPrice;
    private final int chestplatePrice;
    private final int leggingsPrice;
    private final int bootsPrice;
    public ShardObj(String shardTypeName, String armorTypeName,int helmetPrice, int chestplatePrice,int leggingsPrice, int bootsPrice, int menuSlot){
    this.helmetPrice = helmetPrice;
        this.chestplatePrice = chestplatePrice;
        this.leggingsPrice = leggingsPrice;
        this.bootsPrice = bootsPrice;
    this.armorTypeName = armorTypeName;
    this.menuSlot = menuSlot;
    this.shardTypeName = shardTypeName;
    }

    public int getMenuSlot() {
        return menuSlot;
    }

    public int getHelmetPrice() {
        return helmetPrice;
    }

    public int getBootsPrice() {
        return bootsPrice;
    }

    public int getChestplatePrice() {
        return chestplatePrice;
    }

    public int getLeggingsPrice() {
        return leggingsPrice;
    }

    public String getArmorTypeName() {
        return armorTypeName;
    }

    public String getShardTypeName() {
        return shardTypeName;
    }
}
