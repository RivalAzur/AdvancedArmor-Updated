package me.ilucah.advancedarmor.boosting.model;

import me.ilucah.advancedarmor.armor.Armor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.nbt.NBTUtils;
import org.bukkit.entity.Player;

public class BoostService {

    private final Handler handler;

    public BoostService(Handler handler) {
        this.handler = handler;
    }

    public double calculatePercentage(BoostType type, Player player) {
        int percentage = 0;
        if (player.getInventory().getHelmet() != null) {
            if (NBTUtils.hasArmorNBTTag(player.getInventory().getHelmet())) {
                Armor armor = handler.getArmorFromString(NBTUtils.getArmorName(player.getInventory().getHelmet()));
                if (armor != null) {
                    if (armor.getBoostType() == type)
                        percentage += armor.getHelmetBoost();
                }
            }
        }
        if (player.getInventory().getChestplate() != null) {
            if (NBTUtils.hasArmorNBTTag(player.getInventory().getChestplate())) {
                Armor armor = handler.getArmorFromString(NBTUtils.getArmorName(player.getInventory().getChestplate()));
                if (armor != null) {
                    if (armor.getBoostType() == type)
                        percentage += armor.getChestplateBoost();
                }
            }
        }
        if (player.getInventory().getLeggings() != null) {
            if (NBTUtils.hasArmorNBTTag(player.getInventory().getLeggings())) {
                Armor armor = handler.getArmorFromString(NBTUtils.getArmorName(player.getInventory().getLeggings()));
                if (armor != null) {
                    if (armor.getBoostType() == type)
                        percentage += armor.getLeggingsBoost();
                }
            }
        }
        if (player.getInventory().getBoots() != null) {
            if (NBTUtils.hasArmorNBTTag(player.getInventory().getBoots())) {
                Armor armor = handler.getArmorFromString(NBTUtils.getArmorName(player.getInventory().getBoots()));
                if (armor != null) {
                    if (armor.getBoostType() == type)
                        percentage += armor.getBootsBoost();
                }
            }
        }
        double total = (double) percentage / 100;
        total++;
        return total;
    }
}
