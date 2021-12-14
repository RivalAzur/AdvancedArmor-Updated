package me.ilucah.advancedarmor.utilities;

import me.ilucah.advancedarmor.handler.Handler;
import org.bukkit.inventory.ItemStack;

public class ExpUtils {

    private final Handler handler;

    public ExpUtils(Handler handler) {
        this.handler = handler;
    }

    public double calculatePercentage(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        NBTUtils nbtUtils = new NBTUtils(handler);
        int percentage = 0;
        if (helmet != null) {
            if (nbtUtils.hasArmorNBTTag(helmet)) {
                percentage += handler.getArmorFromString(nbtUtils.getArmorName(helmet)).getHelmetBoost();
            }
        }
        if (chestplate != null) {
            if (nbtUtils.hasArmorNBTTag(chestplate)) {
                percentage += handler.getArmorFromString(nbtUtils.getArmorName(chestplate)).getChestplateBoost();
            }
        }
        if (leggings != null) {
            if (nbtUtils.hasArmorNBTTag(leggings)) {
                percentage += handler.getArmorFromString(nbtUtils.getArmorName(leggings)).getLeggingsBoost();
            }
        }
        if (boots != null) {
            if (nbtUtils.hasArmorNBTTag(boots)) {
                percentage += handler.getArmorFromString(nbtUtils.getArmorName(boots)).getBootsBoost();
            }
        }
        if (percentage < 1)
            percentage++;
        double total = (double) percentage / 100;
        if (total < 1)
            total++;
        return total;
    }

    public int calculateRawArmorMultiPercentage(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        NBTUtils nbtUtils = new NBTUtils(handler);
        int percentage = 0;
        if (helmet != null) {
            if (nbtUtils.hasArmorNBTTag(helmet)) {
                percentage += handler.getArmorFromString(nbtUtils.getArmorName(helmet)).getHelmetBoost();
            }
        }
        if (chestplate != null) {
            if (nbtUtils.hasArmorNBTTag(chestplate)) {
                percentage += handler.getArmorFromString(nbtUtils.getArmorName(chestplate)).getChestplateBoost();
            }
        }
        if (leggings != null) {
            if (nbtUtils.hasArmorNBTTag(leggings)) {
                percentage += handler.getArmorFromString(nbtUtils.getArmorName(leggings)).getLeggingsBoost();
            }
        }
        if (boots != null) {
            if (nbtUtils.hasArmorNBTTag(boots)) {
                percentage += handler.getArmorFromString(nbtUtils.getArmorName(boots)).getBootsBoost();
            }
        }
        if (percentage < 1)
            percentage++;
        return percentage;
    }
}
