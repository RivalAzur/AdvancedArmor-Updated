package me.ilucah.advancedarmor.handler.apimanager;

import me.ilucah.advancedarmor.armor.ArmorType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.ExpUtils;
import me.ilucah.advancedarmor.utilities.NBTUtils;
import me.ilucah.advancedarmor.utilities.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ExperiencePlayer {

    private ExpUtils expUtils;
    private NBTUtils nbtUtils;

    private Player player;

    public ExperiencePlayer(Handler handler, Player player) {
        this.player = player;
        this.expUtils = new ExpUtils(handler);
        this.nbtUtils = new NBTUtils(handler);
    }

    public ExperiencePlayer(Handler handler, OfflinePlayer player) {
        this.player = player.getPlayer();
        this.expUtils = new ExpUtils(handler);
        this.nbtUtils = new NBTUtils(handler);
    }

    public ExperiencePlayer(Handler handler, String player) {
        this.player = Bukkit.getPlayer(player);
        this.expUtils = new ExpUtils(handler);
        this.nbtUtils = new NBTUtils(handler);
    }

    public ExperiencePlayer(Handler handler, UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.expUtils = new ExpUtils(handler);
        this.nbtUtils = new NBTUtils(handler);
    }

    // Get Set

    public Player getPlayer() {
        return this.player;
    }

    public int getExperienceLevel() {
        return this.player.getExpToLevel();
    }

    public int getTotalExperience() {
        return this.player.getTotalExperience();
    }

    public float getExp() {
        return this.player.getExp();
    }

    public void setTotalExperience(int amount) {
        this.player.setTotalExperience(amount);
    }

    public void setExperience(float amount) {
        this.player.setExp(amount);
    }

    public void giveUnmultipliedExperience(int amount) {
        this.player.giveExp(amount);
    }

    public void giveMultipliedExperience(int amount) {
        this.player.giveExp((int) (amount * getCalculatedPercentagePlayerArmorExpBoost()));
    }

    public boolean hasCustomArmorEquipped() {
        if (nbtUtils.hasArmorNBTTag(player.getInventory().getBoots()))
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getChestplate()))
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getLeggings()))
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getHelmet()))
            return true;
        else
            return false;
    }

    public String getPlayerArmorType(Placeholders placeholders) {
        if (nbtUtils.hasArmorNBTTag(player.getInventory().getBoots()))
            return nbtUtils.getArmorName(player.getInventory().getBoots());
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getChestplate()))
            return nbtUtils.getArmorName(player.getInventory().getChestplate());
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getLeggings()))
            return nbtUtils.getArmorName(player.getInventory().getLeggings());
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getHelmet()))
            return nbtUtils.getArmorName(player.getInventory().getHelmet());
        else
            return placeholders.getNoArmorEquipped();
    }

    public String getPlayerArmorType(ArmorType type, Placeholders placeholders) {
        if (type == ArmorType.HELMET) {
            if (nbtUtils.hasArmorNBTTag(player.getInventory().getHelmet()))
                return nbtUtils.getArmorName(player.getInventory().getHelmet());
        } else if (type == ArmorType.CHESTPLATE) {
            if (nbtUtils.hasArmorNBTTag(player.getInventory().getChestplate()))
                return nbtUtils.getArmorName(player.getInventory().getChestplate());
        } else if (type == ArmorType.LEGGINGS) {
            if (nbtUtils.hasArmorNBTTag(player.getInventory().getLeggings()))
                return nbtUtils.getArmorName(player.getInventory().getLeggings());
        } else if (type == ArmorType.BOOTS) {
            if (nbtUtils.hasArmorNBTTag(player.getInventory().getBoots()))
                return nbtUtils.getArmorName(player.getInventory().getBoots());
        }
        return placeholders.getNoArmorEquipped();
    }

    public int getRawPlayerArmorExpBoost() {
        return this.expUtils.calculateRawArmorMultiPercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
    }

    public double getCalculatedPercentagePlayerArmorExpBoost() {
        return this.expUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
    }

}