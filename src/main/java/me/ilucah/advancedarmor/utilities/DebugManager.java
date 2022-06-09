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

    public void runDebug(Player player, double amount, double afterAmount) {
        if (!isEnabled())
            return;
        if (main.getConfig().getBoolean("Debug.Player")) {
            player.sendMessage("%debug% - open");
            player.sendMessage("%debug% Amount: " + amount);
            player.sendMessage("%debug% New Amount: " + afterAmount);
            player.sendMessage("%debug% - close");
        }
        if (main.getConfig().getBoolean("Debug.Console")) {
            Bukkit.getLogger().info("%debug% - open");
            Bukkit.getLogger().info("%debug% - Amount: " + amount);
            Bukkit.getLogger().info("%debug% - New Amount: " + afterAmount);
            Bukkit.getLogger().info("%debug% - close");
        }
    }

}
