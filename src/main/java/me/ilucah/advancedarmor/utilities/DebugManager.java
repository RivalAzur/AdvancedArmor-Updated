package me.ilucah.advancedarmor.utilities;

import me.ilucah.advancedarmor.AdvancedArmor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DebugManager {

    private AdvancedArmor main;

    public DebugManager(AdvancedArmor main) {
        this.main = main;
    }

    public boolean isEnabled() {
        if (main.getConfig().getBoolean("Debug.Enabled"))
            return true;
        return false;
    }

    public void expEventDebugInfoSend (int expAmount, int giveExpAmount, int totalExpAmount, double expBoost) {
        if (main.getConfig().getBoolean("Debug.Console")) {
            Bukkit.getLogger().info("%debug% - open");
            Bukkit.getLogger().info("%debug% onExpChangeEvent has been run");
            Bukkit.getLogger().info("%debug% ");
            Bukkit.getLogger().info("%debug% Boost Amount: " + expBoost);
            Bukkit.getLogger().info("%debug% EXP Before Boost: " + expAmount);
            Bukkit.getLogger().info("%debug% EXP Boost Amount: " + giveExpAmount);
            Bukkit.getLogger().info("%debug% EXP Total: " + totalExpAmount);
            Bukkit.getLogger().info("%debug% - close");
        }
    }
    public void expEventDebugInfoSend (Player player, int expAmount, int giveExpAmount, int totalExpAmount, double expBoost) {
        if (main.getConfig().getBoolean("Debug.Player")) {
            player.sendMessage("%debug% - open");
            player.sendMessage("%debug% onExpChangeEvent has been run");
            player.sendMessage("%debug% ");
            player.sendMessage("%debug% Boost Amount: " + expBoost);
            player.sendMessage("%debug% EXP Before Boost: " + expAmount);
            player.sendMessage("%debug% EXP Boost Amount: " + giveExpAmount);
            player.sendMessage("%debug% EXP Total: " + totalExpAmount);
            player.sendMessage("%debug% - close");
        }
    }

    public void moneyEventDebugInfoSend (double moneyChangeAmount, double giveMoneyAmount, double totalMoneyAmount, double moneyBoost) {
        if (main.getConfig().getBoolean("Debug.Console")) {
            Bukkit.getLogger().info("%debug% - open");
            Bukkit.getLogger().info("%debug% onMoneyChangeEvent has been run");
            Bukkit.getLogger().info("%debug% ");
            Bukkit.getLogger().info("%debug% Boost Amount: " + moneyBoost);
            Bukkit.getLogger().info("%debug% Balance Before Boost: " + moneyChangeAmount);
            Bukkit.getLogger().info("%debug% Balance Boost Amount: " + giveMoneyAmount);
            Bukkit.getLogger().info("%debug% Balance Total: " + totalMoneyAmount);
            Bukkit.getLogger().info("%debug% - close");
        }
    }

    public void moneyEventDebugInfoSend (Player player, double moneyChangeAmount, double giveMoneyAmount, double totalMoneyAmount, double moneyBoost) {
        if (main.getConfig().getBoolean("Debug.Player")) {
            player.sendMessage("%debug% - open");
            player.sendMessage("%debug% onMoneyChangeEvent has been run");
            player.sendMessage("%debug% ");
            player.sendMessage("%debug% Boost Amount: " + moneyBoost);
            player.sendMessage("%debug% Balance Before Boost: " + moneyChangeAmount);
            player.sendMessage("%debug% Balance Boost Amount: " + giveMoneyAmount);
            player.sendMessage("%debug% Balance Total: " + totalMoneyAmount);
            player.sendMessage("%debug% - close");
        }
    }

    public void coinEventDebugInfoSend (double coinChangeAmount, double giveCoinAmount, double totalCoinAmount, double coinBoost) {
        if (main.getConfig().getBoolean("Debug.Console")) {
            Bukkit.getLogger().info("%debug% - open");
            Bukkit.getLogger().info("%debug% onMoneyChangeEvent has been run");
            Bukkit.getLogger().info("%debug% ");
            Bukkit.getLogger().info("%debug% Boost Amount: " + coinBoost);
            Bukkit.getLogger().info("%debug% Balance Before Boost: " + coinChangeAmount);
            Bukkit.getLogger().info("%debug% Balance Boost Amount: " + giveCoinAmount);
            Bukkit.getLogger().info("%debug% Balance Total: " + totalCoinAmount);
            Bukkit.getLogger().info("%debug% - close");
        }
    }

    public void coinEventDebugInfoSend (Player player, double coinChangeAmount, double giveCoinAmount, double totalCoinAmount, double coinBoost) {
        if (main.getConfig().getBoolean("Debug.Player")) {
            player.sendMessage("%debug% - open");
            player.sendMessage("%debug% onCoinChangeEvent has been run");
            player.sendMessage("%debug% ");
            player.sendMessage("%debug% Boost Amount: " + coinBoost);
            player.sendMessage("%debug% Balance Before Boost: " + coinChangeAmount);
            player.sendMessage("%debug% Balance Boost Amount: " + giveCoinAmount);
            player.sendMessage("%debug% Balance Total: " + totalCoinAmount);
            player.sendMessage("%debug% - close");
        }
    }

    // Unused. Unset
    public void percentageCalculationInfoSend (Player player, int a, int b, double c, double d) {
        player.sendMessage("%debug% - open");
        player.sendMessage("%debug% onExpPercentageCalculation has been run");
        player.sendMessage("%debug%");
        player.sendMessage("%debug% percentage 1: " + a);
        player.sendMessage("%debug% percentage 2: " + b);
        player.sendMessage("%debug% total 1: " + c);
        player.sendMessage("%debug% total 2: " + d);
        player.sendMessage("%debug% - close");
    }

    // Unused. Unset
    public void percentageCalculationInfoSend (int a, int b, double c, double d) {
        Bukkit.getLogger().info("%debug% - open");
        Bukkit.getLogger().info("%debug% onExpPercentageCalculation has been run");
        Bukkit.getLogger().info("%debug%");
        Bukkit.getLogger().info("%debug% percentage 1: " + a);
        Bukkit.getLogger().info("%debug% percentage 2: " + b);
        Bukkit.getLogger().info("%debug% total 1: " + c);
        Bukkit.getLogger().info("%debug% total 2: " + d);
        Bukkit.getLogger().info("%debug% - close");
    }
}
