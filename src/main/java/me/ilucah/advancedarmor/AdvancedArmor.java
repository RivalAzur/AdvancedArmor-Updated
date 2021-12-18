package me.ilucah.advancedarmor;

import me.ilucah.advancedarmor.armor.listeners.ExperienceHandling;
import me.ilucah.advancedarmor.armor.listeners.EssentialsMoneyListener;
import me.ilucah.advancedarmor.armor.listeners.ShopGUIPlusListener;
import me.ilucah.advancedarmor.utilities.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.ilucah.advancedarmor.commands.ArmorCommand;
import me.ilucah.advancedarmor.handler.Handler;

public class AdvancedArmor extends JavaPlugin {

    private Handler handler;

    @Override
    public void onEnable() {
        this.handler = new Handler(this);

        registerEvents();
        registerCommands();
        registerPlaceholderAPI();

        loadConfig();
        handler.initialiseColors();
        handler.initialiseArmor();

        getLogger().info("Please note that in order to change economy providers in the config, you need to reload the server.");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new ExperienceHandling(handler, this), this);
        registerEssentials();
        registerShopGUIPlus();
    }

    private void registerCommands() {
        getCommand("armor").setExecutor(new ArmorCommand(this));
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void registerPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(handler, this).register();
        }
    }

    public void registerShopGUIPlus() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.ShopGUIPlus-Enabled")) {
            if (getServer().getPluginManager().getPlugin("ShopGuiPlus") != null
                    || getServer().getPluginManager().getPlugin("ShopGui+") != null) {
                getServer().getPluginManager().registerEvents(new ShopGUIPlusListener(this), this);
                Bukkit.getLogger().info("[AdvancedArmor] Successfully hooked into ShopGui+");
            } else {
                Bukkit.getLogger().info("[AdvancedArmor] Failed to hook into ShopGUIPlus. Money component disabled.");
            }
        }
    }

    public void registerEssentials() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.Essentials-Enabled")) {
            if (getServer().getPluginManager().getPlugin("Essentials") != null || getServer().getPluginManager().getPlugin("EssentialsX") != null) {
                getServer().getPluginManager().registerEvents(new EssentialsMoneyListener(handler, this), this);
                Bukkit.getLogger().info("[AdvancedArmor] Successfully hooked into EssentialsX");
            } else {
                Bukkit.getLogger().info("[AdvancedArmor] Failed to hook into EssentialsX. Money component disabled.");
            }
        }
    }

    //

    public Handler getHandler() {
        return this.handler;
    }

}
