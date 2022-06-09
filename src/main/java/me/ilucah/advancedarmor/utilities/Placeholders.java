package me.ilucah.advancedarmor.utilities;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
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
        if (params.contains("boost_")) {
            BoostType type = BoostType.valueOf(params.split("boost_")[0]);
            return String.valueOf(plugin.getHandler().getBoostService().calculatePercentage(type, offlinePlayer.getPlayer()));
        }
        return "This placeholder no longer exists."; // Placeholder is unknown by the expansion
    }

}