package me.ilucah.advancedarmor;

import me.ilucah.advancedarmor.api.AdvancedArmorAPI;
import me.ilucah.advancedarmor.armor.lib.*;
import me.ilucah.advancedarmor.boosting.providers.*;
import me.ilucah.advancedarmor.listener.*;
import me.ilucah.advancedarmor.placeholders.Placeholders;
import me.ilucah.advancedarmor.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.ilucah.advancedarmor.commands.ArmorCommand;
import me.ilucah.advancedarmor.handler.Handler;

public class AdvancedArmor extends JavaPlugin {

    private Handler handler;
    private ConfigManager configManager;

    private AdvancedArmorAPI api;
    public static AdvancedArmor instance;
    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        configManager.load();
        instance = this;
        this.handler = new Handler(this);

        this.api = new AdvancedArmorAPI(handler);
        registerEvents();
        getCommand("armor").setExecutor(new ArmorCommand(this));
        getCommand("armor").setTabCompleter(new ArmorCommand(this));
        registerPlaceholderAPI();

        handler.initialiseTranslations();
        handler.initialiseColors();
        handler.initialiseArmor();
        getServer().getPluginManager().registerEvents(new ShardListener(), this);
        ArmorEquipEvent.registerListener(this);
        getLogger().info("Please note that in order to change economy providers in the config, you need to reload the server.");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new SkullPlaceListener(), this);
        registerEssentials();
        registerShopGUIPlus();
        registerSuperMobCoins();
        registerClipAutoSell();
        registerDeluxeSellWandsMoney();
        registerDeluxeSellWandsTokens();
        registerTMMobcoins();
        registerScyther();
        registerEconShopGUI();
        registerRevEnchants();
        registerWildTools();
        registerXPrisonCore();
        registerEDPrisonCore();
        registerManifestCollector();
        new ExperienceProvider(this);
    }

    private void registerPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new Placeholders(this).register();
    }

    private void registerShopGUIPlus() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.ShopGUIPlus-Enabled")) {
            if (getServer().getPluginManager().getPlugin("ShopGUIPlus") != null
                    || getServer().getPluginManager().getPlugin("ShopGUI+") != null) {
                new ShopGUIPlusProvider(this);
                getLogger().info("Successfully hooked into ShopGui+");
            } else
                getLogger().warning("Failed to hook into ShopGUIPlus. Money component disabled.");
        }
    }

    private void registerEssentials() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.Essentials-Enabled")) {
            if (getServer().getPluginManager().getPlugin("Essentials") != null || getServer().getPluginManager().getPlugin("EssentialsX") != null) {
                new EssentialsMoneyProvider(this);
                getLogger().info("Successfully hooked into EssentialsX");
            } else
                getLogger().warning("Failed to hook into EssentialsX. Money component disabled.");
        }
    }
    private void registerXPrisonCore() {
        if (getServer().getPluginManager().getPlugin("X-Prison") != null) {
            if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.X-Prison-Enabled")) {
                new XPCMoneyProvider(this);
                getLogger().info("Successfully hooked into X-Prison MoneyAPI");
            }
            if (getConfig().getBoolean("Token-Armor.Economy-Dependencies.X-Prison-Enabled")) {
              new XPCTokenProvider(this);
                getLogger().info("Successfully hooked into X-Prison TokenAPI");
            }
            if (getConfig().getBoolean("Gem-Armor.Economy-Dependencies.X-Prison-Enabled")) {
               new XPCGemProvider(this);
                getLogger().info("Successfully hooked into X-Prison GemAPI");
            }
        }
    }
    private void registerManifestCollector() {
        if (getServer().getPluginManager().getPlugin("ManifestCollector") != null) {
            if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.ManifestCollector-Enabled")) {
                    new ManifestCollectorProvider(this);
                getLogger().info("Successfully hooked into ManifestCollector MoneyAPI");
            }
        }

    }
    private void registerEDPrisonCore() {
        if (getServer().getPluginManager().getPlugin("EdPrison") != null) {
            if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.EdPrison-Enabled")) {
new EDPrisonMoneyProvider(this);
                getLogger().info("Successfully hooked into EdPrison MoneyAPI");
            }
            if (getConfig().getBoolean("Token-Armor.Economy-Dependencies.EdPrison-Enabled")) {
                new EDPrisonTokenProvider(this);
                getLogger().info("Successfully hooked into EdPrison TokenAPI");
            }
            if (getConfig().getBoolean("Gem-Armor.Economy-Dependencies.EdPrison-Enabled")) {
                new EDPrisonGemsProvider(this);
                getLogger().info("Successfully hooked into EdPrison GemAPI");
            }
        }
    }



    private void registerRevEnchants() {
        if (getServer().getPluginManager().getPlugin("RevEnchants") != null) {
            if (getConfig().getBoolean("Token-Armor.Economy-Dependencies.RevEnchants-Enabled")) {
                new RevEnchantsTokenProvider(this);
                getLogger().info("Successfully hooked into RevEnchants TokenAPI");
            }
            if (getConfig().getBoolean("Gem-Armor.Economy-Dependencies.RevEnchants-Enabled")) {
                new RevEnchantsGemProvider(this);
                getLogger().info("Successfully hooked into RevEnchants GemAPI");
            }
        }
    }

    private void registerSuperMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.SuperMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("SuperMobCoins") != null) {
                new SuperMobCoinProvider(this);
                getLogger().info("Successfully hooked into SuperMobCoins");
            } else
                getLogger().warning("Failed to hook into SuperMobCoins. Coin component disabled.");
        }
    }




    private void registerClipAutoSell() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.ClipAutoSell-Enabled")) {
            if (getServer().getPluginManager().getPlugin("AutoSell") != null && getServer().getPluginManager().getPlugin("Vault") != null) {
                new ClipAutosellProvider(this);
                getLogger().info("Successfully hooked into AutoSell");
            } else
                getLogger().warning("Failed to hook into AutoSell. Money component disabled.");
        }
    }

    private void registerDeluxeSellWandsMoney() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.DeluxeSellWands-Enabled")) {
            if (getServer().getPluginManager().getPlugin("DeluxeSellwands") != null) {
                new DSWMoneyProvider(this);
                getLogger().info("Successfully hooked into DeluxeSellwands Money");
            } else
                getLogger().warning("Failed to hook into DeluxeSellwands Money. Money component disabled.");
        }
    }

    private void registerDeluxeSellWandsTokens() {
        if (getConfig().getBoolean("Token-Armor.Economy-Dependencies.DeluxeSellWands-Enabled")) {
            if (getServer().getPluginManager().getPlugin("DeluxeSellwands") != null) {
                new DSWTokenProvider(this);
                getLogger().info("Successfully hooked into DeluxeSellwands Tokens");
            } else
                getLogger().warning("Failed to hook into DeluxeSellwands Tokens. Token component disabled.");
        }
    }

    private void registerScyther() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.Scyther-Enabled")) {
            if (getServer().getPluginManager().getPlugin("Scyther") != null) {
                new ScytherMoneyProvider(this);
                getLogger().info("Successfully hooked into Scyther Tokens");
            } else
                getLogger().warning("Failed to hook into Scyther Tokens. Money component disabled.");
        }
    }

    private void registerTMMobcoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.TMMobcoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("TMMobCoins") != null) {
                new TMCoinProvider(this);
                getLogger().info("Successfully hooked into TMMobCoins");
            } else
                getLogger().warning("Failed to hook into TMMobCoins. Coin component disabled.");
        }
    }

    private void registerEconShopGUI() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.EconomyShopGUI-Enabled")) {
            if (getServer().getPluginManager().getPlugin("EconomyShopGUI") != null || getServer().getPluginManager().getPlugin("EconomyShopGUI-Premium") != null) {
                new EconomyShopProvider(this);
                getLogger().info("Successfully hooked into EconomyShopGUI");
            } else
                getLogger().warning("Failed to hook into EconomyShopGUI. Money component disabled.");
        }
    }

    private void registerWildTools() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.WildTools-Enabled")) {
            if (getServer().getPluginManager().getPlugin("WildTools") != null) {
                new WildToolsProvider(this);
                getLogger().info("Successfully hooked into WildTools");
            } else
                getLogger().warning("Failed to hook into WildTools. Money component disabled.");
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

    public AdvancedArmorAPI getAPI() {
        return api;
    }
}
