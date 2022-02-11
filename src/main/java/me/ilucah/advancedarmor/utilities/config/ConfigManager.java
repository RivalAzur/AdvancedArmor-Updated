package me.ilucah.advancedarmor.utilities.config;

import me.ilucah.advancedarmor.AdvancedArmor;

import java.io.File;
import java.util.ArrayList;

public class ConfigManager {

    private final AdvancedArmor plugin;
    private final DefaultConfigurationHandler configHandler;

    public ConfigManager(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.configHandler = new DefaultConfigurationHandler(plugin);
    }

    public void load() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (configFile.exists())
            return;
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    // Unoperational
    private void copyDefaults() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
        } else {
            ArrayList<String> removablePaths = new ArrayList<String>();
            configHandler.getDefaultConfigurationSections().iterator().forEachRemaining(path -> {
                if (plugin.getConfig().getConfigurationSection("Armor.Types." + path) == null) {
                    removablePaths.add("Armor.Types." + path);
                }
            });
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            removablePaths.iterator().forEachRemaining(path -> {
                plugin.getConfig().set(path, null);
                plugin.saveConfig();
            });
        }
    }
}
