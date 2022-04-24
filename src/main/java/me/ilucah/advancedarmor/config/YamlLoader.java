package me.ilucah.advancedarmor.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class YamlLoader {

    private String yamlName;

    private File dataFolder;
    private JavaPlugin plugin;

    public YamlLoader(String yamlName, JavaPlugin plugin, File dataFolder) {
        this.yamlName = yamlName;
        this.dataFolder = dataFolder;
        this.plugin = plugin;
    }

    public YamlLoader(String yamlName, JavaPlugin plugin) {
        this(yamlName, plugin, plugin.getDataFolder());
    }

    public FileConfiguration load(boolean copyDefaults) {
        try {
            if (!dataFolder.exists())
                dataFolder.mkdirs();
            File file = new File(dataFolder, yamlName + ".yml");
            if (!file.exists()) {
                if (plugin.getResource(yamlName + ".yml") != null)
                    plugin.saveResource(yamlName + ".yml", false);
                else
                    file.createNewFile();
            }
            return YamlConfiguration.loadConfiguration(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FileConfiguration loadMessageFile() {
        try {
            if (!dataFolder.exists())
                dataFolder.mkdirs();
            File file = new File(dataFolder + "/messages/", yamlName + ".yml");
            boolean isResource = plugin.getResource("messages/" + yamlName + ".yml") != null;
            if (!file.exists()) {
                if (isResource) {
                    plugin.saveResource("messages/" + yamlName + ".yml", false);
                } else {
                    file.createNewFile();
                }
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}