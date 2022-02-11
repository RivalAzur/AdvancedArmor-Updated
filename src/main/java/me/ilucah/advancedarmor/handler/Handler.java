package me.ilucah.advancedarmor.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ilucah.advancedarmor.armor.Flag;
import me.ilucah.advancedarmor.utilities.DebugManager;
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

    private final DebugManager debugManager;

    public Handler(AdvancedArmor plugin) {
        this.plugin = plugin;

        this.armorColors = new ArrayList<ArmorColor>();
        this.armor = new ArrayList<Armor>();
        this.armorMapped = new HashMap<String, Armor>();

        this.debugManager = new DebugManager(plugin);
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
                    enchants.put(EnchantmentUtils.getEnchantment(enchant), plugin.getConfig().getInt("Armor.Types." + type + ".Enchants." + enchant));
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
                    color, enchants, itemFlags);
            armor.add(a);
            armorMapped.put(type, a);
        }
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
}
