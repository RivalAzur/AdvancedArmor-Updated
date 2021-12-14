package me.ilucah.advancedarmor.utilities;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.handler.apimanager.ExperiencePlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Placeholders extends PlaceholderExpansion {

    private AdvancedArmor plugin;
    private Handler handler;

    public Placeholders(Handler handler, AdvancedArmor plugin) {
        this.plugin = plugin;
        this.handler = handler;
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
        ExperiencePlayer player = new ExperiencePlayer(handler, offlinePlayer);
        if (params.equalsIgnoreCase("armortype")) {
            if (player.hasCustomArmorEquipped()) {
                return player.getPlayerArmorType();
            }
        }
        if (params.equalsIgnoreCase("armorboost")) {
            if (player.hasCustomArmorEquipped()) {
                return String.valueOf(player.getRawPlayerArmorExpBoost());
            }
        }
        return null; // Placeholder is unknown by the expansion
    }

}