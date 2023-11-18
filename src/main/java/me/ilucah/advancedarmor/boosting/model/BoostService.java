package me.ilucah.advancedarmor.boosting.model;

import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.armor.Armor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.nbt.NBTUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.*;

public class BoostService {

    private final Handler handler;
    private Map<UUID, Map<BoostType, Double>> cachedValues = new ConcurrentHashMap<>();
    public BoostService(Handler handler) {
        this.handler = handler;
        new BukkitRunnable() {
            @Override
            public void run() {
            clearCache();
            }
        }.runTaskTimer(AdvancedArmor.instance,10L, 100L);

    }


    public void clearCache() {
        cachedValues.clear();
    }


    public double calculatePercentage(BoostType type, Player player) {


        if (cachedValues.containsKey(player.getUniqueId()) && cachedValues.get(player.getUniqueId()).containsKey(type)) {
            return cachedValues.get(player.getUniqueId()).get(type);
        }

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
        cachedValues.computeIfAbsent(player.getUniqueId(), k -> new ConcurrentHashMap<>()).put(type, total);
        return total;
    }



    @Nullable
    public String getArmorName(ItemStack item) {
        if (item == null)
            return null;
        if (!NBTUtils.hasArmorNBTTag(item))
            return null;
        Armor armor = handler.getArmorFromString(NBTUtils.getArmorName(item));
        return armor != null ? armor.getName() : null;
    }

    public double getArmorBoost(ItemStack item) {
        int percentage = 0;
        if (item != null) {
            if (NBTUtils.hasArmorNBTTag(item)) {
                Armor armor = handler.getArmorFromString(NBTUtils.getArmorName(item));
                if (armor != null)
                    percentage += armor.getHelmetBoost();
            }
        }
        double total = (double) percentage / 100;
        total++;
        return total;
    }
}
