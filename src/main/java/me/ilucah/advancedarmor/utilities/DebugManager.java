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
