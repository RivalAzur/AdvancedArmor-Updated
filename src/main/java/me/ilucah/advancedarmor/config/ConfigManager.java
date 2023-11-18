package me.ilucah.advancedarmor.config;

import me.ilucah.advancedarmor.AdvancedArmor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final AdvancedArmor plugin;

    private FileConfiguration config, armor, messages,shards;

    public ConfigManager(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    public void load() {
        config = new YamlLoader("config", plugin).load(true);
        armor = new YamlLoader("armor", plugin).load(true);
        shards = new YamlLoader("shards", plugin).load(true);
        messages = new YamlLoader("messages", plugin).load(true);
    }

    public FileConfiguration getArmor() {
        return armor;
    }
    public FileConfiguration getShards() {
        return shards;
    }
    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessages() {
        return messages;
    }
}
