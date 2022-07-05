package me.ilucah.advancedarmor;

import me.ilucah.advancedarmor.api.AdvancedArmorAPI;
import me.ilucah.advancedarmor.boosting.providers.*;
import me.ilucah.advancedarmor.listener.SkullPlaceListener;
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

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        configManager.load();
        this.handler = new Handler(this);

        this.api = new AdvancedArmorAPI(handler);
        registerEvents();
        getCommand("armor").setExecutor(new ArmorCommand(this));
        registerPlaceholderAPI();

        handler.initialiseTranslations();
        handler.initialiseColors();
        handler.initialiseArmor();

        getLogger().info("Please note that in order to change economy providers in the config, you need to reload the server.");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new SkullPlaceListener(), this);
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
        registerScyther();
        registerRivalHoes();
        registerRivalRods();
        registerRivalSwords();
        registerEconShopGUI();
        registerTokensGC();
        registerRevEnchants();
        registerWildTools();
        registerRivalHoesMoney();
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

    private void registerInfiniteChestPro() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.InfiniteChestPro-Enabled")) {
            if (getServer().getPluginManager().getPlugin("InfiniteChest-Pro") != null) {
                new InfiniteChestProProvider(this);
                getLogger().info("Successfully hooked into InfiniteChest-Pro");
            } else
                getLogger().warning("Failed to hook into InfiniteChest-Pro. Money component disabled.");
        }
    }

    private void registerUltraPrisonCore() {
        if (getServer().getPluginManager().getPlugin("UltraPrisonCore") != null) {
            if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.UltraPrisonCore-Enabled")) {
                new UPCMoneyProvider(this);
                getLogger().info("Successfully hooked into UltraPrisonCore MoneyAPI");
            }
            if (getConfig().getBoolean("Token-Armor.Economy-Dependencies.UltraPrisonCore-Enabled")) {
                new UPCTokenProvider(this);
                getLogger().info("Successfully hooked into UltraPrisonCore TokenAPI");
            }
            if (getConfig().getBoolean("Gem-Armor.Economy-Dependencies.UltraPrisonCore-Enabled")) {
                new UPCGemProvider(this);
                getLogger().info("Successfully hooked into UltraPrisonCore GemAPI");
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

    private void registerKrakenMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.KrakenMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("KrakenMobcoins") != null) {
                new KrakenMobCoinProvider(this);
                getLogger().info("Successfully hooked into KrakenMobCoins");
            } else
                getLogger().warning("Failed to hook into KrakenMobCoins. Coin component disabled.");
        }
    }

    private void registerTheOnlyMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.TheOnlyMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("TheOnly-MobCoins") != null) {
                try {
                    new OnlyMobCoinProvider(this);
                } catch (NoClassDefFoundError e) {
                    getLogger().warning("Failed to hook into TheOnlyMobCoins, MythicMobs Required. Coin component disabled.");
                    return;
                }
                getLogger().info("Successfully hooked into TheOnlyMobCoins");
            } else
                getLogger().warning("Failed to hook into TheOnlyMobCoins. Coin component disabled.");
        }
    }

    private void registerQuadrexMobCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.QuadrexMobCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("QuadrexMobCoins") != null) {
                new QuadrexCoinProvider(this);
                getLogger().info("Successfully hooked into QuadrexMobCoins");
            } else
                getLogger().warning("Failed to hook into KrakenMobCoins. Coin component disabled.");
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

    private void registerUltraBackpacks() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.UltraBackpacks-Enabled")) {
            if (getServer().getPluginManager().getPlugin("UltraBackpacks") != null && getServer().getPluginManager().getPlugin("UltraBackpacks") != null) {
                new UltraBackpacksProvider(this);
                getLogger().info("Successfully hooked into UltraBackpacks");
            } else
                getLogger().warning("Failed to hook into UltraBackpacks. Money component disabled.");
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

    private void registerAquaCoins() {
        if (getConfig().getBoolean("Coin-Armor.Economy-Dependencies.AquaCoins-Enabled")) {
            if (getServer().getPluginManager().getPlugin("AquaCoins") != null) {
                new AquaCoinProvider(this);
                getLogger().info("Successfully hooked into AquaCoins");
            } else
                getLogger().warning("Failed to hook into AquaCoins. Coin component disabled.");
        }
    }

    private void registerRivalHoes() {
        if (getConfig().getBoolean("Essence-Armor.Economy-Dependencies.RivalHarvesterHoes-Enabled")) {
            if (getServer().getPluginManager().getPlugin("RivalHarvesterHoes") != null) {
                new RivalHoesProvider(this);
                getLogger().info("Successfully hooked into RivalHarvesterHoes");
            } else
                getLogger().warning("Failed to hook into RivalHarvesterHoes. Essence component disabled.");
        }
    }

    private void registerRivalHoesMoney() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.RivalHarvesterHoes-Enabled")) {
            if (getServer().getPluginManager().getPlugin("RivalHarvesterHoes") != null) {
                new RivalHoesMoneyProvider(this);
                getLogger().info("Successfully hooked into RivalHarvesterHoes");
            } else
                getLogger().warning("Failed to hook into RivalHarvesterHoes. Money component disabled.");
        }
    }

    private void registerRivalRods() {
        if (getConfig().getBoolean("Essence-Armor.Economy-Dependencies.RivalFishingRods-Enabled")) {
            if (getServer().getPluginManager().getPlugin("RivalFishingRods") != null) {
                new RivalFishingProvider(this);
                getLogger().info("Successfully hooked into RivalFishingRods");
            } else
                getLogger().warning("Failed to hook into RivalFishingRods. Essence component disabled.");
        }
    }

    private void registerRivalSwords() {
        if (getConfig().getBoolean("Essence-Armor.Economy-Dependencies.RivalMobSwords-Enabled")) {
            if (getServer().getPluginManager().getPlugin("RivalMobSwords") != null) {
                new RivalSwordsProvider(this);
                getLogger().info("Successfully hooked into RivalMobSwords");
            } else
                getLogger().warning("Failed to hook into RivalMobSwords. Essence component disabled.");
        }
    }

    private void registerEconShopGUI() {
        if (getConfig().getBoolean("Money-Armor.Economy-Dependencies.EconomyShopGUI-Enabled")) {
            if (getServer().getPluginManager().getPlugin("EconomyShopGUI") != null) {
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

    private void registerTokensGC() {
        if (getConfig().getBoolean("Token-Armor.Economy-Dependencies.Tokens-GC-Enabled")) {
            if (getServer().getPluginManager().getPlugin("Tokens") != null) {
                new GCTokensProvider(this);
                getLogger().info("Successfully hooked into Tokens");
            } else
                getLogger().warning("Failed to hook into Tokens. Token component disabled.");
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
