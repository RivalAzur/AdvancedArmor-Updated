package me.ilucah.advancedarmor.utilities.boost;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.NBTUtils;
import org.bukkit.inventory.ItemStack;

public class TokenUtils {

    private final Handler handler;

    public TokenUtils(AdvancedArmor plugin) {
        this.handler = plugin.getHandler();
    }

    public TokenUtils(Handler handler) {
        this.handler = handler;
    }

    public double calculatePercentage(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        NBTUtils nbtUtils = new NBTUtils(handler);
        int percentage = 0;
        if (helmet != null) {
            if (nbtUtils.hasArmorNBTTag(helmet)) {
                if (handler.getArmorFromString(nbtUtils.getArmorName(helmet)).getBoostType() == BoostType.TOKEN)
                    percentage += handler.getArmorFromString(nbtUtils.getArmorName(helmet)).getHelmetBoost();
            }
        }
        if (chestplate != null) {
            if (nbtUtils.hasArmorNBTTag(chestplate)) {
                if (handler.getArmorFromString(nbtUtils.getArmorName(chestplate)).getBoostType() == BoostType.TOKEN)
                    percentage += handler.getArmorFromString(nbtUtils.getArmorName(chestplate)).getChestplateBoost();
            }
        }
        if (leggings != null) {
            if (nbtUtils.hasArmorNBTTag(leggings)) {
                if (handler.getArmorFromString(nbtUtils.getArmorName(leggings)).getBoostType() == BoostType.TOKEN)
                    percentage += handler.getArmorFromString(nbtUtils.getArmorName(leggings)).getLeggingsBoost();
            }
        }
        if (boots != null) {
            if (nbtUtils.hasArmorNBTTag(boots)) {
                if (handler.getArmorFromString(nbtUtils.getArmorName(boots)).getBoostType() == BoostType.TOKEN)
                    percentage += handler.getArmorFromString(nbtUtils.getArmorName(boots)).getBootsBoost();
            }
        }
        double total = (double) percentage / 100;
        total++;
        return total;
    }

    public int calculateRawArmorMultiPercentage(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        NBTUtils nbtUtils = new NBTUtils(handler);
        int percentage = 0;
        if (helmet != null) {
            if (nbtUtils.hasArmorNBTTag(helmet)) {
                if (handler.getArmorFromString(nbtUtils.getArmorName(helmet)).getBoostType() == BoostType.TOKEN)
                    percentage += handler.getArmorFromString(nbtUtils.getArmorName(helmet)).getHelmetBoost();
            }
        }
        if (chestplate != null) {
            if (nbtUtils.hasArmorNBTTag(chestplate)) {
                if (handler.getArmorFromString(nbtUtils.getArmorName(chestplate)).getBoostType() == BoostType.TOKEN)
                    percentage += handler.getArmorFromString(nbtUtils.getArmorName(chestplate)).getChestplateBoost();
            }
        }
        if (leggings != null) {
            if (nbtUtils.hasArmorNBTTag(leggings)) {
                if (handler.getArmorFromString(nbtUtils.getArmorName(leggings)).getBoostType() == BoostType.TOKEN)
                    percentage += handler.getArmorFromString(nbtUtils.getArmorName(leggings)).getLeggingsBoost();
            }
        }
        if (boots != null) {
            if (nbtUtils.hasArmorNBTTag(boots)) {
                if (handler.getArmorFromString(nbtUtils.getArmorName(boots)).getBoostType() == BoostType.TOKEN)
                    percentage += handler.getArmorFromString(nbtUtils.getArmorName(boots)).getBootsBoost();
            }
        }
        if (percentage == 1)
            percentage++;
        return percentage;
    }
}
