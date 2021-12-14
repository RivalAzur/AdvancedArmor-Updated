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

    public void expEventDebugInfoSend (int expAmount, int giveExpAmount, int totalExpAmount) {
        if (main.getConfig().getBoolean("Debug.Console")) {
            Bukkit.getLogger().info("%debug% - open");
            Bukkit.getLogger().info("%debug% onExpChangeEvent has been run");
            Bukkit.getLogger().info("%debug% ");
            Bukkit.getLogger().info("%debug% EXP Before Boost: " + expAmount);
            Bukkit.getLogger().info("%debug% EXP Boost Amount: " + giveExpAmount);
            Bukkit.getLogger().info("%debug% EXP Total: " + totalExpAmount);
            Bukkit.getLogger().info("%debug% - close");
        }
    }
    public void expEventDebugInfoSend (Player player, int expAmount, int giveExpAmount, int totalExpAmount) {
        if (main.getConfig().getBoolean("Debug.Player")) {
            player.sendMessage("%debug% - open");
            player.sendMessage("%debug% onExpChangeEvent has been run");
            player.sendMessage("%debug% ");
            player.sendMessage("%debug% EXP Before Boost: " + expAmount);
            player.sendMessage("%debug% EXP Boost Amount: " + giveExpAmount);
            player.sendMessage("%debug% EXP Total: " + totalExpAmount);
            player.sendMessage("%debug% - close");
        }
    }

    // Unused. Unset
    public void expPercentageCalculationInfoSend (Player player, int a, int b, double c, double d) {
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
    public void expPercentageCalculationInfoSend (int a, int b, double c, double d) {
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
