package me.ilucah.advancedarmor.utilities;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.handler.apimanager.ExperiencePlayer;
import me.ilucah.advancedarmor.handler.apimanager.MoneyPlayer;
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
        MoneyPlayer moneyPlayer = new MoneyPlayer(handler, offlinePlayer);
        if (params.equalsIgnoreCase("armortype")) {
            if (player.hasCustomArmorEquipped()) {
                return player.getPlayerArmorType();
            } else {
                return "No Armor Equipped";
            }
        }
        if (params.equalsIgnoreCase("boosttype")) {
            if (player.hasCustomArmorEquipped()) {
                if (handler.getArmorFromString(player.getPlayerArmorType()).getBoostType() == BoostType.EXP)
                    return "EXP";
                else
                    return "MONEY";
            } else {
                return "No Armor Equipped";
            }
        }
        if (params.equalsIgnoreCase("expboost")) {
            if (player.hasCustomArmorEquipped()) {
                return String.valueOf(player.getRawPlayerArmorExpBoost());
            } else {
                return "1";
            }
        }
        if (params.equalsIgnoreCase("moneyboost")) {
            if (player.hasCustomArmorEquipped()) {
                return String.valueOf(moneyPlayer.getRawBoostAmount());
            } else {
                return "1";
            }
        }
        return "Null, contact author"; // Placeholder is unknown by the expansion
    }

}