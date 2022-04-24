package me.ilucah.advancedarmor.armor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.ilucah.advancedarmor.utilities.RGBParser;
import me.ilucah.advancedarmor.utilities.xutils.XMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Armor {

    private String name;
    private String displayName;

    private BoostType boostType;
    private int bootsBoost;
    private int leggingsBoost;
    private int chestplateBoost;
    private int helmetBoost;

    private List<String> armorLore;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ArmorColor color;
    private ItemStack[] armorBaseMaterialItems;

    private String[] translations;

    private Map<Enchantment, Integer> enchants;
    private List<Flag> itemFlags;

    public Armor(String name, String displayName, BoostType boostType, int helmetBoost, int chestBoost, int legsBoost,
                 int bootsBoost, List<String> armorLore, ArmorColor color, Map<Enchantment, Integer> enchants, List<Flag> itemFlags, String[] translations, ItemStack[] baseArmorItems) {
        this.name = name;
        this.displayName = displayName;
        this.boostType = boostType;

        this.bootsBoost = bootsBoost;
        this.leggingsBoost = legsBoost;
        this.chestplateBoost = chestBoost;
        this.helmetBoost = helmetBoost;

        this.armorLore = new ArrayList<String>();

        for (String string : armorLore) {
            this.armorLore.add(RGBParser.parse(string));
        }

        this.armorBaseMaterialItems = baseArmorItems;
        this.translations = translations == null ? createTranslations() : translations;
        this.enchants = enchants;
        this.itemFlags = itemFlags;
        this.color = color;

        initialiseHelmet();
        initialiseChestplate();
        initialiseLeggings();
        initialiseBoots();
    }

    public void initialiseHelmet() {
        ItemStack helmet = armorBaseMaterialItems[0].clone();
        ItemMeta meta = helmet.getItemMeta();
        meta.setDisplayName(RGBParser.parse(this.displayName.replace("%armor_type%", translations[0])));
        meta.setLore(this.armorLore);
        if (helmet.getItemMeta() instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) meta).setColor(color.getColor());
        Flag.addItemFlags(itemFlags, meta);
        helmet.setItemMeta(meta);
        helmet.addUnsafeEnchantments(enchants);
        NBTItem nbtItem = new NBTItem(helmet);
        nbtItem.setString("CustomArmor", this.name);
        this.helmet = nbtItem.getItem();
    }

    public void initialiseChestplate() {
        ItemStack chestplate = armorBaseMaterialItems[1].clone();
        ItemMeta meta = chestplate.getItemMeta();
        meta.setDisplayName(RGBParser.parse(this.displayName.replace("%armor_type%", translations[1])));
        meta.setLore(this.armorLore);
        if (chestplate.getItemMeta() instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) meta).setColor(color.getColor());
        Flag.addItemFlags(itemFlags, meta);
        chestplate.setItemMeta(meta);
        chestplate.addUnsafeEnchantments(enchants);
        NBTItem nbtItem = new NBTItem(chestplate);
        nbtItem.setString("CustomArmor", this.name);
        this.chestplate = nbtItem.getItem();
    }

    public void initialiseLeggings() {
        ItemStack leggings = armorBaseMaterialItems[2].clone();
        ItemMeta meta = leggings.getItemMeta();
        this.displayName.replace("%armor_type%", translations[2]);
        meta.setDisplayName(RGBParser.parse(this.displayName.replace("%armor_type%", translations[2])));
        meta.setLore(this.armorLore);
        if (leggings.getItemMeta() instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) meta).setColor(color.getColor());
        Flag.addItemFlags(itemFlags, meta);
        leggings.setItemMeta(meta);
        leggings.addUnsafeEnchantments(enchants);
        NBTItem nbtItem = new NBTItem(leggings);
        nbtItem.setString("CustomArmor", this.name);
        this.leggings = nbtItem.getItem();
    }

    public void initialiseBoots() {
        ItemStack boots = armorBaseMaterialItems[3].clone();
        ItemMeta meta = boots.getItemMeta();
        meta.setDisplayName(RGBParser.parse(this.displayName.replace("%armor_type%", translations[3])));
        meta.setLore(this.armorLore);
        if (boots.getItemMeta() instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) meta).setColor(color.getColor());
        Flag.addItemFlags(itemFlags, meta);
        boots.setItemMeta(meta);
        boots.addUnsafeEnchantments(enchants);
        NBTItem nbtItem = new NBTItem(boots);
        nbtItem.setString("CustomArmor", this.name);
        this.boots = nbtItem.getItem();
    }

    //

    public ItemStack getItemStackFromType(ArmorType armorType) {
        if (armorType == ArmorType.HELMET) {
            return this.helmet;
        } else if (armorType == ArmorType.CHESTPLATE) {
            return this.chestplate;
        } else if (armorType == ArmorType.LEGGINGS) {
            return this.leggings;
        } else {
            return this.boots;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public ArmorColor getColor() {
        return color;
    }

    public void setColor(ArmorColor color) {
        this.color = color;
    }

    public BoostType getBoostType() {
        return boostType;
    }

    public void setBoostType(BoostType boostType) {
        this.boostType = boostType;
    }

    public int getBootsBoost() {
        return bootsBoost;
    }

    public void setBootsBoost(int bootsBoost) {
        this.bootsBoost = bootsBoost;
    }

    public int getLeggingsBoost() {
        return leggingsBoost;
    }

    public void setLeggingsBoost(int leggingsBoost) {
        this.leggingsBoost = leggingsBoost;
    }

    public int getChestplateBoost() {
        return chestplateBoost;
    }

    public void setChestplateBoost(int chestplateBoost) {
        this.chestplateBoost = chestplateBoost;
    }

    public int getHelmetBoost() {
        return helmetBoost;
    }

    public void setHelmetBoost(int helmetBoost) {
        this.helmetBoost = helmetBoost;
    }

    public List<String> getArmorLore() {
        return armorLore;
    }

    public void setArmorLore(List<String> armorLore) {
        this.armorLore = armorLore;
    }

    public List<Flag> getItemFlags() {
        return this.itemFlags;
    }

    private String[] createTranslations() {
        String[] translations = new String[4];
        translations[0] = "Helmet";
        translations[1] = "Chestplate";
        translations[2] = "Leggings";
        translations[3] = "Boots";
        return translations;
    }

}
