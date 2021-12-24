package me.ilucah.advancedarmor;

import me.ilucah.advancedarmor.armor.listeners.*;
import me.ilucah.advancedarmor.utilities.Placeholders;
import me.ilucah.advancedarmor.utilities.config.ConfigManager;
import me.ilucah.advancedarmor.utilities.ichest.HookType;
import me.ilucah.advancedarmor.utilities.ichest.IChestHookManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.ilucah.advancedarmor.commands.ArmorCommand;
import me.ilucah.advancedarmor.handler.Handler;

public class AdvancedArmor extends JavaPlugin {

    private Handler handler;

    @Override
    public void onEnable() {
        new ConfigManager(this).load();
        this.handler = new Handler(this);

        registerEvents();
        registerCommands();
        registerPlaceholderAPI();

        handler.initialiseColors();
        handler.initialiseArmor();

        getLogger().info("Please note that in order to change economy providers in the config, you need to reload the server.");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new ExperienceHandling(handler, this), this);
        registerEssentials();
        registerShopGUIPlus();
        registerInfiniteChestPro();
        registerUltraPrisonCore();
        registerSuperMobCoins();
    }

    private void registerCommands() {
        getCommand("armor").setExecutor(new ArmorCommand(this));
    }

    private void registerPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(handler, this).register();
        }
    }

    private void registerShopGUIPlus() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.ShopGUIPlus-Enabled")) {
            if (getServer().getPluginManager().getPlugin("ShopGUIPlus") != null
                    || getServer().getPluginManager().getPlugin("ShopGUI+") != null) {
                getServer().getPluginManager().registerEvents(new ShopGUIPlusListener(this), this);
                getLogger().info("Successfully hooked into ShopGui+");
            } else {
                getLogger().warning("Failed to hook into ShopGUIPlus. Money component disabled.");
            }
        }
    }

    private void registerEssentials() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.Essentials-Enabled")) {
            if (getServer().getPluginManager().getPlugin("Essentials") != null || getServer().getPluginManager().getPlugin("EssentialsX") != null) {
                getServer().getPluginManager().registerEvents(new EssentialsMoneyListener(handler, this), this);
                getLogger().info("Successfully hooked into EssentialsX");
            } else {
                getLogger().warning("Failed to hook into EssentialsX. Money component disabled.");
            }
        }
    }

    private void registerInfiniteChestPro() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.InfiniteChestPro.Enabled")) {
            if (getServer().getPluginManager().getPlugin("InfiniteChest-Pro") != null) {
                HookType hookType;
                if (getConfig().getString("Money-Armor.Economy-Dependencies.InfiniteChestPro.HookType").equalsIgnoreCase("Essentials")) {
                    hookType = HookType.ESSENTIALS;
                } else if (getConfig().getString("Money-Armor.Economy-Dependencies.InfiniteChestPro.HookType").equalsIgnoreCase("Shopguiplus") ||
                        getConfig().getString("Money-Armor.Economy-Dependencies.InfiniteChestPro.HookType").equalsIgnoreCase("shopgui+")) {
                    hookType = HookType.SHOPGUIPLUS;
                } else {
                    getLogger().warning("Failed to register InfiniteChestsPro hook. Please use 'Essentials' or 'ShopGUIPlus' as a HookType.");
                    return;
                }
                IChestHookManager hookManager = new IChestHookManager(this, hookType);
                getServer().getPluginManager().registerEvents(new InfiniteChestProListener(this, hookManager), this);
                getLogger().info("Successfully hooked into InfiniteChest-Pro");
            } else {
                getLogger().warning("Failed to hook into InfiniteChest-Pro. Money component disabled.");
            }
        }
    }

    private void registerUltraPrisonCore() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.UltraPrisonCore-Enabled")) {
            if (getServer().getPluginManager().getPlugin("UltraPrisonCore") != null) {
                getServer().getPluginManager().registerEvents(new UltraPrisonCoreListener(this), this);
                getLogger().info("Successfully hooked into UltraPrisonCore");
            } else {
                getLogger().warning("Failed to hook into UltraPrisonCore. Money component disabled.");
            }
        }
    }

    private void registerSuperMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.SuperMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("SuperMobCoins") != null) {
                getServer().getPluginManager().registerEvents(new SuperMobCoinListener(this), this);
                getLogger().info("Successfully hooked into SuperMobCoins");
            } else {
                getLogger().warning("Failed to hook into SuperMobCoins. Money component disabled.");
            }
        }
    }

    //

    public Handler getHandler() {
        return this.handler;
    }

}
