package me.ilucah.advancedarmor.utilities.config;

import me.ilucah.advancedarmor.AdvancedArmor;

import java.util.ArrayList;
import java.util.List;

public class DefaultConfigurationHandler {

    private final AdvancedArmor plugin;

    private List<String> defaultConfigurationSections;

    public DefaultConfigurationHandler (AdvancedArmor plugin) {
        this.plugin = plugin;
        initDefaults();
    }

    private void initDefaults() {
        defaultConfigurationSections = new ArrayList<String>();
        defaultConfigurationSections.add("Helix");
        defaultConfigurationSections.add("Rainbow");
        defaultConfigurationSections.add("Fire");
        defaultConfigurationSections.add("PiggyBank");
        defaultConfigurationSections.add("Coin");
    }

    public List<String> getDefaultConfigurationSections() {
        return this.defaultConfigurationSections;
    }
}
