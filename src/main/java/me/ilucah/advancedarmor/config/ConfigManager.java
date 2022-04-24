package me.ilucah.advancedarmor.config;

import me.ilucah.advancedarmor.AdvancedArmor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final AdvancedArmor plugin;

    private FileConfiguration config, armor;

    public ConfigManager(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    public void load() {
        config = new YamlLoader("config", plugin).load(true);
        armor = new YamlLoader("armor", plugin).load(true);
    }

    public FileConfiguration getArmor() {
        return armor;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
