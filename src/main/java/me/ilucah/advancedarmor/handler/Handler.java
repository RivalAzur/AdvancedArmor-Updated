package me.ilucah.advancedarmor.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ilucah.advancedarmor.armor.Flag;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageManager;
import org.bukkit.enchantments.Enchantment;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.Armor;
import me.ilucah.advancedarmor.armor.ArmorColor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.utilities.EnchantmentUtils;
import org.bukkit.inventory.ItemFlag;

public class Handler {

    private AdvancedArmor plugin;

    private List<ArmorColor> armorColors;
    private List<Armor> armor;
    private Map<String, Armor> armorMapped;
    private String[] translations;

    private final DebugManager debugManager;
    private final MessageManager messageManager;

    public Handler(AdvancedArmor plugin) {
        this.plugin = plugin;

        this.armorColors = new ArrayList<ArmorColor>();
        this.armor = new ArrayList<Armor>();
        this.armorMapped = new HashMap<String, Armor>();
        this.translations = new String[4];

        this.debugManager = new DebugManager(plugin);
        this.messageManager = new MessageManager(plugin);
    }

    public void initialiseColors() {
        for (String color : plugin.getConfig().getConfigurationSection("ArmorColor").getKeys(false)) {
            int r = plugin.getConfig().getInt("ArmorColor." + color + ".R");
            int g = plugin.getConfig().getInt("ArmorColor." + color + ".G");
            int b = plugin.getConfig().getInt("ArmorColor." + color + ".B");
            armorColors.add(new ArmorColor(color, r, g, b));
        }
    }

    public void initialiseArmor() {
        for (String type : plugin.getConfig().getConfigurationSection("Armor.Types").getKeys(false)) {
            String name = plugin.getConfig().getString("Armor.Types." + type + ".Name");

            BoostType boostType = BoostType
                    .valueOf(plugin.getConfig().getString("Armor.Types." + type + ".Boost.Type"));
            int bootsBoost = plugin.getConfig().getInt("Armor.Types." + type + ".Boost.Boots-Percentage");
            int leggingsBoost = plugin.getConfig().getInt("Armor.Types." + type + ".Boost.Leggings-Percentage");
            int chestplateBoost = plugin.getConfig().getInt("Armor.Types." + type + ".Boost.Chestplate-Percentage");
            int helmetBoost = plugin.getConfig().getInt("Armor.Types." + type + ".Boost.Helmet-Percentage");

            List<String> armorLore = plugin.getConfig().getStringList("Armor.Types." + type + ".Lore");
            ArmorColor color = ArmorColor.valueOf(plugin, plugin.getConfig().getString("Armor.Types." + type + ".ArmorColor"));

            Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
            if (plugin.getConfig().getConfigurationSection("Armor.Types." + type + ".Enchants") != null) {
                for (String enchant : plugin.getConfig().getConfigurationSection("Armor.Types." + type + ".Enchants").getKeys(false)) {
                    try {
                        enchants.put(EnchantmentUtils.getEnchantment(enchant), plugin.getConfig().getInt("Armor.Types." + type + ".Enchants." + enchant));
                    } catch (NullPointerException exception) {
                        plugin.getLogger().warning("The enchantment: " + enchant + " failed to load for the armor type: " + type + ".");
                        plugin.getLogger().warning("This is a known issue, it may be a result of your server version.");
                    }
                }
            }

            List<Flag> itemFlags = new ArrayList<Flag>();
            if (plugin.getConfig().getConfigurationSection("Armor.Types." + type + ".Flags") != null) {
                for (String itemFlag : plugin.getConfig().getConfigurationSection("Armor.Types." + type + ".Flags").getKeys(false)) {
                    if (plugin.getConfig().getBoolean("Armor.Types." + type + ".Flags." + itemFlag)) {
                        if (itemFlag.contains("Unbreakable"))
                            itemFlags.add(new Flag(null, true));
                        else
                            itemFlags.add(new Flag(ItemFlag.valueOf(itemFlag), false));
                    }
                }
            }
            Armor a = new Armor(type, name, boostType, helmetBoost, chestplateBoost, leggingsBoost, bootsBoost, armorLore,
                    color, enchants, itemFlags, translations);
            armor.add(a);
            armorMapped.put(type, a);
        }
    }

    public void initialiseTranslations() {
        if (plugin.getConfig().getConfigurationSection("Translations").getKeys(false) != null) {
            translations[0] = plugin.getConfig().getString("Translations.Helmet");
            translations[1] = plugin.getConfig().getString("Translations.Chestplate");
            translations[2] = plugin.getConfig().getString("Translations.Leggings");
            translations[3] = plugin.getConfig().getString("Translations.Boots");
            return;
        } else {
            translations[0] = "Helmet";
            translations[1] = "Chestplate";
            translations[2] = "Leggings";
            translations[3] = "Boots";
        }
    }

    public void reloadCaches() {
        this.armorColors = new ArrayList<ArmorColor>();
        this.armor = new ArrayList<Armor>();
        this.armorMapped = new HashMap<String, Armor>();

        initialiseColors();
        initialiseArmor();
    }

    public List<ArmorColor> getArmorColors() {
        return this.armorColors;
    }

    public List<Armor> getArmor() {
        return this.armor;
    }

    public Armor getArmorFromString(String name) {
        return this.armorMapped.get(name);
    }

    public DebugManager getDebugManager() {
        return debugManager;
    }

    public String[] getTranslations() {
        return translations;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
