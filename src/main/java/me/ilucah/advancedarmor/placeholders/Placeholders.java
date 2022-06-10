package me.ilucah.advancedarmor.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Placeholders extends PlaceholderExpansion {

    private AdvancedArmor plugin;
    private String noArmorEquipped;

    public Placeholders(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.noArmorEquipped = RGBParser.parse(plugin.getConfig().getString("Translations.Placeholders.No-Armor-Equipped"));
    }

    public String getNoArmorEquipped() {
        return noArmorEquipped;
    }

    @Override
    public String getIdentifier() {
        return "advancedarmor";
    }

    @Override
    public String getAuthor() {
        return "iLucaH";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getRequiredPlugin() {
        return "AdvancedArmor";
    }

    @Override
    public boolean canRegister() {
        return (plugin = (AdvancedArmor) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        if (params.equalsIgnoreCase("helmet_boost"))
            return String.valueOf(plugin.getHandler().getBoostService().getArmorBoost(offlinePlayer.getPlayer().getInventory().getHelmet()));
        if (params.equalsIgnoreCase("chestplate_boost"))
            return String.valueOf(plugin.getHandler().getBoostService().getArmorBoost(offlinePlayer.getPlayer().getInventory().getChestplate()));
        if (params.equalsIgnoreCase("leggings_boost"))
            return String.valueOf(plugin.getHandler().getBoostService().getArmorBoost(offlinePlayer.getPlayer().getInventory().getLeggings()));
        if (params.equalsIgnoreCase("boots_boost"))
            return String.valueOf(plugin.getHandler().getBoostService().getArmorBoost(offlinePlayer.getPlayer().getInventory().getBoots()));
        if (params.equalsIgnoreCase("helmet_type")) {
            String name = plugin.getHandler().getBoostService().getArmorName(offlinePlayer.getPlayer().getInventory().getHelmet());
            return name == null ? noArmorEquipped : name;
        }
        if (params.equalsIgnoreCase("chestplate_type")) {
            String name = plugin.getHandler().getBoostService().getArmorName(offlinePlayer.getPlayer().getInventory().getChestplate());
            return name == null ? noArmorEquipped : name;
        }
        if (params.equalsIgnoreCase("leggings_type")) {
            String name = plugin.getHandler().getBoostService().getArmorName(offlinePlayer.getPlayer().getInventory().getChestplate());
            return name == null ? noArmorEquipped : name;
        }
        if (params.equalsIgnoreCase("boots_type")) {
            String name = plugin.getHandler().getBoostService().getArmorName(offlinePlayer.getPlayer().getInventory().getBoots());
            return name == null ? noArmorEquipped : name;
        }
        if (params.contains("boost_")) {
            BoostType type = BoostType.valueOf(params.split("boost_")[0].toUpperCase());
            return String.valueOf(plugin.getHandler().getBoostService().calculatePercentage(type, offlinePlayer.getPlayer()));
        }
        return "This placeholder no longer exists."; // Placeholder is unknown by the expansion
    }
}