package me.ilucah.advancedarmor;

import me.ilucah.advancedarmor.armor.listeners.*;
import me.ilucah.advancedarmor.utilities.Placeholders;
import me.ilucah.advancedarmor.config.ConfigManager;
import me.ilucah.advancedarmor.utilities.ichest.HookType;
import me.ilucah.advancedarmor.utilities.ichest.IChestHookManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.ilucah.advancedarmor.commands.ArmorCommand;
import me.ilucah.advancedarmor.handler.Handler;

public class AdvancedArmor extends JavaPlugin {

    private Handler handler;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        configManager.load();
        this.handler = new Handler(this);

        registerEvents();
        registerCommands();
        registerPlaceholderAPI();

        handler.initialiseTranslations();
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
        registerKrakenMobCoins();
        registerTheOnlyMobCoins();
        registerQuadrexMobCoins();
        registerClipAutoSell();
        registerUltraBackpacks();
        registerDeluxeSellWandsMoney();
        registerDeluxeSellWandsTokens();
        registerTMMobcoins();
        registerAquaCoins();
    }

    private void registerCommands() {
        getCommand("armor").setExecutor(new ArmorCommand(this));
    }

    private void registerPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
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
        if (getServer().getPluginManager().getPlugin("UltraPrisonCore") != null) {
            if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.UltraPrisonCore-Enabled")) {
                getServer().getPluginManager().registerEvents(new UltraPrisonCoreListener(this), this);
                getLogger().info("Successfully hooked into UltraPrisonCore MoneyAPI");
            }
            if (getConfig().getBoolean("Token-Armor.Economy-Dependencies.UltraPrisonCore-Enabled")) {
                getServer().getPluginManager().registerEvents(new UPCTokenListener(this), this);
                getLogger().info("Successfully hooked into UltraPrisonCore TokenAPI");
            }
            if (getConfig().getBoolean("Gem-Armor.Economy-Dependencies.UltraPrisonCore-Enabled")) {
                getServer().getPluginManager().registerEvents(new UPCGemListener(this), this);
                getLogger().info("Successfully hooked into UltraPrisonCore GemAPI");
            }
        } else {
            getLogger().warning("If UltraPrisonCoreHook import was disabled, ignore this message;");
            getLogger().warning("UPC failed to registered.");
        }
    }

    private void registerSuperMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.SuperMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("SuperMobCoins") != null) {
                getServer().getPluginManager().registerEvents(new SuperMobCoinListener(this), this);
                getLogger().info("Successfully hooked into SuperMobCoins");
            } else {
                getLogger().warning("Failed to hook into SuperMobCoins. Coin component disabled.");
            }
        }
    }

    private void registerKrakenMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.KrakenMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("KrakenMobcoins") != null) {
                getServer().getPluginManager().registerEvents(new KrakenMobCoinsListener(this), this);
                getLogger().info("Successfully hooked into KrakenMobCoins");
            } else {
                getLogger().warning("Failed to hook into KrakenMobCoins. Coin component disabled.");
            }
        }
    }

    private void registerTheOnlyMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.TheOnlyMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("TheOnly-MobCoins") != null) {
                try {
                    getServer().getPluginManager().registerEvents(new TheOnlyMobCoinsListener(this), this);
                } catch (NoClassDefFoundError e) {
                    getLogger().warning("Failed to hook into TheOnlyMobCoins, MythicMobs Required. Coin component disabled.");
                    return;
                }
                getLogger().info("Successfully hooked into TheOnlyMobCoins");
            } else {
                getLogger().warning("Failed to hook into TheOnlyMobCoins. Coin component disabled.");
            }
        }
    }

    private void registerQuadrexMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.QuadrexMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("QuadrexMobCoins") != null) {
                getServer().getPluginManager().registerEvents(new QuadrexMobCoinsListener(this), this);
                getLogger().info("Successfully hooked into QuadrexMobCoins");
            } else {
                getLogger().warning("Failed to hook into KrakenMobCoins. Coin component disabled.");
            }
        }
    }

    private void registerClipAutoSell() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.ClipAutoSell-Enabled")) {
            if (getServer().getPluginManager().getPlugin("AutoSell") != null && getServer().getPluginManager().getPlugin("Vault") != null) {
                getServer().getPluginManager().registerEvents(new ClipAutoSellListener(this), this);
                getLogger().info("Successfully hooked into AutoSell");
            } else {
                getLogger().warning("Failed to hook into AutoSell. Money component disabled.");
            }
        }
    }

    private void registerUltraBackpacks() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.UltraBackpacks-Enabled")) {
            if (getServer().getPluginManager().getPlugin("UltraBackpacks") != null && getServer().getPluginManager().getPlugin("UltraBackpacks") != null) {
                getServer().getPluginManager().registerEvents(new UltraBackpackListener(this), this);
                getLogger().info("Successfully hooked into UltraBackpacks");
            } else {
                getLogger().warning("Failed to hook into UltraBackpacks. Money component disabled.");
            }
        }
    }

    private void registerDeluxeSellWandsMoney() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.DeluxeSellWands-Enabled")) {
            if (getServer().getPluginManager().getPlugin("DeluxeSellwands") != null) {
                getServer().getPluginManager().registerEvents(new DeluxSellWandsMoneyListener(this), this);
                getLogger().info("Successfully hooked into DeluxeSellwands Money");
            } else {
                getLogger().warning("Failed to hook into DeluxeSellwands Money. Money component disabled.");
            }
        }
    }

    private void registerDeluxeSellWandsTokens() {
        if (getConfig().getBoolean("Token-Armor.Economy-Dependencies.DeluxeSellWands-Enabled")) {
            if (getServer().getPluginManager().getPlugin("DeluxeSellwands") != null) {
                getServer().getPluginManager().registerEvents(new DeluxSellWandsTokenListener(this), this);
                getLogger().info("Successfully hooked into DeluxeSellwands Tokens");
            } else {
                getLogger().warning("Failed to hook into DeluxeSellwands Tokens. Token component disabled.");
            }
        }
    }

    private void registerScyther() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.Scyther-Enabled")) {
            if (getServer().getPluginManager().getPlugin("Scyther") != null) {
                getServer().getPluginManager().registerEvents(new ScytherSellListener(this), this);
                getLogger().info("Successfully hooked into Scyther Tokens");
            } else {
                getLogger().warning("Failed to hook into Scyther Tokens. Money component disabled.");
            }
        }
    }

    private void registerTMMobcoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.TMMobcoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("TMMobCoins") != null) {
                getServer().getPluginManager().registerEvents(new TMMobcoinsListener(this), this);
                getLogger().info("Successfully hooked into TMMobCoins");
            } else {
                getLogger().warning("Failed to hook into TMMobCoins. Coin component disabled.");
            }
        }
    }

    private void registerAquaCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.AquaCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("AquaCoins") != null) {
                getServer().getPluginManager().registerEvents(new AquaCoinsListener(this), this);
                getLogger().info("Successfully hooked into AquaCoins");
            } else {
                getLogger().warning("Failed to hook into AquaCoins. Coin component disabled.");
            }
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public FileConfiguration getConfig() {
        return configManager.getConfig();
    }

    public FileConfiguration getArmor() {
        return configManager.getArmor();
    }

    public Handler getHandler() {
        return this.handler;
    }

}
