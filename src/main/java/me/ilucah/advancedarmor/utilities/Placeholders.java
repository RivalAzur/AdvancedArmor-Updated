package me.ilucah.advancedarmor.utilities;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.ArmorType;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.*;
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
        ExperiencePlayer player = new ExperiencePlayer(plugin.getHandler(), offlinePlayer);
        MoneyPlayer moneyPlayer = new MoneyPlayer(plugin.getHandler(), offlinePlayer);
        if (params.equalsIgnoreCase("armortype")) {
            if (player.hasCustomArmorEquipped()) {
                return player.getPlayerArmorType(this);
            } else {
                return noArmorEquipped;
            }
        }
        if (params.equalsIgnoreCase("helmettype")) {
            if (player.hasCustomArmorEquipped()) {
                return player.getPlayerArmorType(ArmorType.HELMET, this);
            } else {
                return noArmorEquipped;
            }
        }
        if (params.equalsIgnoreCase("chesttype")) {
            if (player.hasCustomArmorEquipped()) {
                return player.getPlayerArmorType(ArmorType.CHESTPLATE, this);
            } else {
                return noArmorEquipped;
            }
        }
        if (params.equalsIgnoreCase("legstype")) {
            if (player.hasCustomArmorEquipped()) {
                return player.getPlayerArmorType(ArmorType.LEGGINGS, this);
            } else {
                return noArmorEquipped;
            }
        }
        if (params.equalsIgnoreCase("bootstype")) {
            if (player.hasCustomArmorEquipped()) {
                return player.getPlayerArmorType(ArmorType.BOOTS, this);
            } else {
                return noArmorEquipped;
            }
        }
        if (params.equalsIgnoreCase("boosttype")) {
            if (player.hasCustomArmorEquipped()) {
                if (plugin.getHandler().getArmorFromString(player.getPlayerArmorType(this)).getBoostType() == BoostType.EXP)
                    return "EXP";
                else if (plugin.getHandler().getArmorFromString(player.getPlayerArmorType(this)).getBoostType() == BoostType.TOKEN)
                    return "TOKEN";
                else if (plugin.getHandler().getArmorFromString(player.getPlayerArmorType(this)).getBoostType() == BoostType.TOKEN)
                    return "COINS";
                else if (plugin.getHandler().getArmorFromString(player.getPlayerArmorType(this)).getBoostType() == BoostType.GEM)
                    return "GEMS";
                else
                    return "MONEY";
            } else {
                return noArmorEquipped;
            }
        }
        if (params.equalsIgnoreCase("expboost")) {
            if (player.hasCustomArmorEquipped()) {
                return String.valueOf(player.getRawPlayerArmorExpBoost());
            } else {
                return "0";
            }
        }
        if (params.equalsIgnoreCase("moneyboost")) {
            if (player.hasCustomArmorEquipped()) {
                return String.valueOf(moneyPlayer.getRawBoostAmount());
            } else {
                return "0";
            }
        }
        if (params.equalsIgnoreCase("tokenboost")) {
            TokenPlayer tokenPlayer = new TokenPlayer(plugin.getHandler(), offlinePlayer);
            if (player.hasCustomArmorEquipped()) {
                return String.valueOf(tokenPlayer.getRawPlayerArmorExpBoost());
            } else {
                return "0";
            }
        }
        if (params.equalsIgnoreCase("coinboost")) {
            CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), offlinePlayer);
            if (player.hasCustomArmorEquipped()) {
                return String.valueOf(coinPlayer.getRawPlayerArmorExpBoost());
            } else {
                return "0";
            }
        }
        if (params.equalsIgnoreCase("gemboost")) {
            GemPlayer coinPlayer = new GemPlayer(plugin.getHandler(), offlinePlayer);
            if (player.hasCustomArmorEquipped()) {
                return String.valueOf(coinPlayer.getRawPlayerArmorExpBoost());
            } else {
                return "0";
            }
        }
        return "Null, contact author"; // Placeholder is unknown by the expansion
    }

}