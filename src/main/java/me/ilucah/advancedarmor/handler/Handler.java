package me.ilucah.advancedarmor.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.ilucah.advancedarmor.armor.Flag;
import me.ilucah.advancedarmor.boosting.BoostService;
import me.ilucah.advancedarmor.boosting.TypeProvider;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageManager;
import me.ilucah.advancedarmor.utilities.xutils.SkullCreator;
import me.ilucah.advancedarmor.utilities.xutils.XMaterial;
import org.bukkit.enchantments.Enchantment;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.Armor;
import me.ilucah.advancedarmor.armor.ArmorColor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.utilities.EnchantmentUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class Handler {

    private AdvancedArmor plugin;

    private List<ArmorColor> armorColors;
    private List<Armor> armor;
    private ConcurrentHashMap<String, Armor> armorMapped;
    private ConcurrentHashMap<Class<? extends Event>, TypeProvider> providers = new ConcurrentHashMap<>();
    private String[] translations;

    private final DebugManager debugManager;
    private final MessageManager messageManager;

    private final BoostService boostService;

    public Handler(AdvancedArmor plugin) {
        this.plugin = plugin;

        this.armorColors = new ArrayList<>();
        this.armor = new ArrayList<>();
        this.armorMapped = new ConcurrentHashMap<>();
        this.translations = new String[4];

        this.debugManager = new DebugManager(plugin);
        this.messageManager = new MessageManager(plugin);
        this.boostService = new BoostService(this);
    }

    public void initialiseColors() {
        for (String color : plugin.getArmor().getConfigurationSection("ArmorColor").getKeys(false)) {
            int r = plugin.getArmor().getInt("ArmorColor." + color + ".R");
            int g = plugin.getArmor().getInt("ArmorColor." + color + ".G");
            int b = plugin.getArmor().getInt("ArmorColor." + color + ".B");
            armorColors.add(new ArmorColor(color, r, g, b));
        }
    }

    public void initialiseArmor() {
        for (String type : plugin.getArmor().getConfigurationSection("Armor.Types").getKeys(false)) {
            String name = plugin.getArmor().getString("Armor.Types." + type + ".Name");

            String boostHandle = plugin.getArmor().getString("Armor.Types." + type + ".Boost.Type");
            BoostType boostType;
            if (boostHandle.equalsIgnoreCase("coins") || boostHandle.equalsIgnoreCase("mobcoins") || boostHandle.equalsIgnoreCase("mobcoin") || boostHandle.equalsIgnoreCase("coin"))
                boostType = BoostType.COIN;
            else if (boostHandle.equalsIgnoreCase("token") || boostHandle.equalsIgnoreCase("tokens"))
                boostType = BoostType.TOKEN;
            else if (boostHandle.equalsIgnoreCase("gem") || boostHandle.equalsIgnoreCase("gems"))
                boostType = BoostType.GEM;
            else if (boostHandle.equalsIgnoreCase("money"))
                boostType = BoostType.MONEY;
            else if (boostHandle.equalsIgnoreCase("exp") || boostHandle.equalsIgnoreCase("experience") || boostHandle.equalsIgnoreCase("xp"))
                boostType = BoostType.EXP;
            else {
                try {
                    boostType = BoostType.valueOf(boostHandle.toUpperCase());
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to load armor set: " + type + ", because " + boostHandle + " is not a known boost type!");
                    continue;
                }
            }
            //BoostType boostType = BoostType
            //        .valueOf(plugin.getArmor().getString("Armor.Types." + type + ".Boost.Type"));
            int bootsBoost = plugin.getArmor().getInt("Armor.Types." + type + ".Boost.Boots-Percentage");
            int leggingsBoost = plugin.getArmor().getInt("Armor.Types." + type + ".Boost.Leggings-Percentage");
            int chestplateBoost = plugin.getArmor().getInt("Armor.Types." + type + ".Boost.Chestplate-Percentage");
            int helmetBoost = plugin.getArmor().getInt("Armor.Types." + type + ".Boost.Helmet-Percentage");

            List<String> armorLore = plugin.getArmor().getStringList("Armor.Types." + type + ".Lore");
            ArmorColor color = ArmorColor.valueOf(plugin, plugin.getArmor().getString("Armor.Types." + type + ".ArmorColor"));

            Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
            if (plugin.getArmor().getConfigurationSection("Armor.Types." + type + ".Enchants") != null) {
                for (String enchant : plugin.getArmor().getConfigurationSection("Armor.Types." + type + ".Enchants").getKeys(false)) {
                    try {
                        enchants.put(EnchantmentUtils.getEnchantment(enchant), plugin.getArmor().getInt("Armor.Types." + type + ".Enchants." + enchant));
                    } catch (NullPointerException exception) {
                        plugin.getLogger().warning("The enchantment: " + enchant + " failed to load for the armor type: " + type + ".");
                        plugin.getLogger().warning("This is a known issue, it may be a result of your server version.");
                    }
                }
            }

            List<Flag> itemFlags = new ArrayList<Flag>();
            if (plugin.getArmor().getConfigurationSection("Armor.Types." + type + ".Flags") != null) {
                for (String itemFlag : plugin.getArmor().getConfigurationSection("Armor.Types." + type + ".Flags").getKeys(false)) {
                    if (plugin.getArmor().getBoolean("Armor.Types." + type + ".Flags." + itemFlag)) {
                        if (itemFlag.contains("Unbreakable"))
                            itemFlags.add(new Flag(null, true));
                        else
                            itemFlags.add(new Flag(ItemFlag.valueOf(itemFlag), false));
                    }
                }
            }
            ItemStack[] baseItemsMaterials = new ItemStack[4];
            if (plugin.getArmor().getString("Armor.Types." + type + ".Items.Helmet.Material") == null)
                baseItemsMaterials[0] = XMaterial.LEATHER_HELMET.parseItem();
            else {
                String mName = plugin.getArmor().getString("Armor.Types." + type + ".Items.Helmet.Material").toUpperCase();
                baseItemsMaterials[0] = XMaterial.valueOf(mName).parseItem();
                if (mName.equalsIgnoreCase("PLAYER_HEAD") || mName.equalsIgnoreCase("SKULL"))
                    baseItemsMaterials[0] = SkullCreator.createSkullFromString(plugin.getArmor().getString("Armor.Types." + type + ".Items.Helmet.Texture"));
            }
            if (plugin.getArmor().getString("Armor.Types." + type + ".Items.Chestplate.Material") == null)
                baseItemsMaterials[1] = XMaterial.LEATHER_CHESTPLATE.parseItem();
            else {
                String mName = plugin.getArmor().getString("Armor.Types." + type + ".Items.Chestplate.Material").toUpperCase();
                baseItemsMaterials[1] = XMaterial.valueOf(mName).parseItem();
            }
            if (plugin.getArmor().getString("Armor.Types." + type + ".Items.Leggings.Material") == null)
                baseItemsMaterials[2] = XMaterial.LEATHER_LEGGINGS.parseItem();
            else {
                String mName = plugin.getArmor().getString("Armor.Types." + type + ".Items.Leggings.Material").toUpperCase();
                baseItemsMaterials[2] = XMaterial.valueOf(mName).parseItem();
            }
            if (plugin.getArmor().getString("Armor.Types." + type + ".Items.Boots.Material") == null)
                baseItemsMaterials[3] = XMaterial.LEATHER_BOOTS.parseItem();
            else {
                String mName = plugin.getArmor().getString("Armor.Types." + type + ".Items.Boots.Material").toUpperCase();
                baseItemsMaterials[3] = XMaterial.valueOf(mName).parseItem();
            }
            Armor a = new Armor(type, name, boostType, helmetBoost, chestplateBoost, leggingsBoost, bootsBoost, armorLore,
                    color, enchants, itemFlags, translations, baseItemsMaterials);
            armor.add(a);
            armorMapped.put(type, a);
            plugin.getLogger().info("Loaded armor set: " + a.getName());
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
        this.armorMapped = new ConcurrentHashMap<String, Armor>();

        initialiseColors();
        initialiseArmor();
    }

    public List<ArmorColor> getArmorColors() {
        return this.armorColors;
    }

    public void addArmor(Armor armor) {
        this.armor.add(armor);
        this.armorMapped.put(armor.getName(), armor);
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

    public BoostService getBoostService() {
        return boostService;
    }

    public ConcurrentHashMap<Class<? extends Event>, TypeProvider> getProviders() {
        return providers;
    }
}
