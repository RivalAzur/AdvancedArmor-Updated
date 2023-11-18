package me.ilucah.advancedarmor.utilities;

import com.iridium.iridiumcolorapi.*;
import com.mojang.authlib.*;
import com.mojang.authlib.properties.*;
import me.ilucah.advancedarmor.utilities.xutils.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;

import java.lang.reflect.*;
import java.util.*;

public class ItemBuilder {
    private ItemStack itemStack;
    private final ItemMeta itemMeta;
    private Map<String, String> placeholders;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        itemStack = XMaterial.matchXMaterial(material).parseItem();
        itemMeta = itemStack.getItemMeta();
        this.placeholders = new HashMap<>();
    }
    public ItemBuilder(String material, int amount) {
        ItemStack itemStack1;
        itemStack1 = XMaterial.matchXMaterial(material).get().parseItem();
        if (itemStack1 == null){
            itemStack1 = new ItemStack(Material.DIAMOND, 1);
        }
        itemStack = itemStack1;
        itemMeta = itemStack.getItemMeta();
        this.placeholders = new HashMap<>();
    }
    private String replacePlaceholders(String input) {
        if (this.placeholders.isEmpty()) {
            return input;
        }

        String output = input;

        for (Map.Entry<String, String> entry : this.placeholders.entrySet()) {
            String placeholder = entry.getKey();
            String value = entry.getValue();

            output = output.replace(placeholder, value);
        }

        return output;
    }

    public ItemBuilder setSkullTexture(String texture) {

        SkullMeta skullMeta = (SkullMeta) itemMeta;

        SkullCreator.mutateItemMeta(skullMeta, texture);
        return this;
    }

    public ItemBuilder setSkullOwner(String player) {

        SkullMeta skullMeta = (SkullMeta) itemMeta;

       skullMeta.setOwner(player);

        itemStack.setItemMeta(skullMeta);
        return this;
    }
    public ItemBuilder setName(String name) {
        itemMeta.setDisplayName(IridiumColorAPI.process( name));
        return this;
    }
    public ItemBuilder setNamePlaceholders(String displayName) {
        if (displayName != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String placeholder = entry.getKey();
                String replacement = entry.getValue();
                displayName = displayName.replace(placeholder, replacement);
            }
            itemMeta.setDisplayName(IridiumColorAPI.process(displayName));
        }
        return this;
    }
    public ItemBuilder setLorePlaceholders(List<String> lore) {
        if (lore != null) {
            List<String> newLore = new ArrayList<>();
            for (String line : lore) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    String placeholder = entry.getKey();
                    String replacement = entry.getValue();
                    line = line.replace(placeholder, replacement);
                }
                newLore.add(IridiumColorAPI.process(line));
            }
            itemMeta.setLore(newLore);
        }
        return this;
    }


    public ItemBuilder setLore(List<String> lore) {
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(IridiumColorAPI.process( line));
        }
        itemMeta.setLore(coloredLore);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder setCustomModelData(Integer customModelData) {
        itemMeta.setCustomModelData(customModelData);
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemMeta).setColor(color);
        } else if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).setColor(color);
        }
        return this;
    }

    public ItemBuilder setPotion(PotionData potionData) {
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).setBasePotionData(potionData);
        }
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }



    public static ItemBuilder fromConfig(ConfigurationSection config) {
        if (config.getString("material") == null) {
            throw new IllegalArgumentException("Missing required config option: material");
        }

        int amount = config.getInt("amount", 1);

        ItemBuilder itemBuilder = new ItemBuilder(config.getString("material").toUpperCase(), amount);

        if (config.isString("name")) {
            String name = IridiumColorAPI.process( config.getString("name"));
            itemBuilder.setName(name);
        }

        if (config.isConfigurationSection("color")) {
            ConfigurationSection colorConfig = config.getConfigurationSection("color");
            int red = colorConfig.getInt("red");
            int green = colorConfig.getInt("green");
            int blue = colorConfig.getInt("blue");
            itemBuilder.setColor(Color.fromRGB(red, green, blue));
        }

        if (config.isList("lore")) {
            List<String> lore = new ArrayList<>();
            for (String line : config.getStringList("lore")) {
                lore.add(IridiumColorAPI.process( line));
            }
            itemBuilder.setLore(lore);
        }

        if (config.isConfigurationSection("enchantments")) {
            ConfigurationSection enchantsConfig = config.getConfigurationSection("enchantments");
            for (String key : enchantsConfig.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName(key);
                int level = enchantsConfig.getInt(key);
                itemBuilder.addEnchantment(enchantment, level);
            }
        }

        if (config.isConfigurationSection("flags")) {
            ConfigurationSection flagsConfig = config.getConfigurationSection("flags");
            for (String key : flagsConfig.getKeys(false)) {
                if (flagsConfig.getBoolean(key)) {
                    ItemFlag flag = ItemFlag.valueOf(key);
                    itemBuilder.addFlags(flag);
                }
            }
        }

        if (config.isBoolean("unbreakable")) {
            itemBuilder.setUnbreakable(config.getBoolean("unbreakable"));
        }

        if (config.isInt("durability")) {
            itemBuilder.setDurability((short) config.getInt("durability"));
        }

        if (config.isInt("customModelData")) {
            itemBuilder.setCustomModelData(config.getInt("customModelData"));
        }


        if (config.isConfigurationSection("potion")) {
            ConfigurationSection potionConfig = config.getConfigurationSection("potion");
            try {
                PotionType potionType = PotionType.valueOf(potionConfig.getString("type"));
                boolean isExtended = potionConfig.getBoolean("extended");
                boolean isUpgraded = potionConfig.getBoolean("upgraded");
                try {
                    PotionData potionData = new PotionData(potionType, isExtended, isUpgraded);
                    itemBuilder.setPotion(potionData);
                } catch (NoClassDefFoundError d) {

                }
            }catch (IllegalArgumentException e){

        }
        }
        if (config.isString("base64")) {
            itemBuilder.setSkullTexture(config.getString("base64"));
        }


        return itemBuilder;
    }
    public static ItemBuilder fromConfigPlaceholder(ConfigurationSection config, Map<String, String> placeholders) {

        if (config.getString("material") == null) {
            throw new IllegalArgumentException("Missing required config option: material");
        }

        int amount = config.getInt("amount", 1);

        ItemBuilder itemBuilder = new ItemBuilder(config.getString("material").toUpperCase(), amount);
        if (!placeholders.isEmpty()) {
            itemBuilder.placeholders.putAll(placeholders);
        }

        if (config.isString("name")) {
            String name = IridiumColorAPI.process(config.getString("name"));
            itemBuilder.setNamePlaceholders(name);
        }

        if (config.isConfigurationSection("color")) {
            ConfigurationSection colorConfig = config.getConfigurationSection("color");
            int red = colorConfig.getInt("red");
            int green = colorConfig.getInt("green");
            int blue = colorConfig.getInt("blue");
            itemBuilder.setColor(Color.fromRGB(red, green, blue));
        }

        if (config.isList("lore")) {
            itemBuilder.setLorePlaceholders(config.getStringList("lore"));
        }

        if (config.isConfigurationSection("enchantments")) {
            ConfigurationSection enchantsConfig = config.getConfigurationSection("enchantments");
            for (String key : enchantsConfig.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName(key);
                int level = enchantsConfig.getInt(key);
                itemBuilder.addEnchantment(enchantment, level);
            }
        }

        if (config.isConfigurationSection("flags")) {
            ConfigurationSection flagsConfig = config.getConfigurationSection("flags");
            for (String key : flagsConfig.getKeys(false)) {
                if (flagsConfig.getBoolean(key)) {
                    ItemFlag flag = ItemFlag.valueOf(key);
                    itemBuilder.addFlags(flag);
                }
            }
        }

        if (config.isBoolean("unbreakable")) {
            itemBuilder.setUnbreakable(config.getBoolean("unbreakable"));
        }

        if (config.isInt("durability")) {
            itemBuilder.setDurability((short) config.getInt("durability"));
        }

        if (config.isInt("customModelData")) {
            itemBuilder.setCustomModelData(config.getInt("customModelData"));
        }

        if (config.isConfigurationSection("potion")) {
            ConfigurationSection potionConfig = config.getConfigurationSection("potion");

            PotionType potionType = PotionType.valueOf(potionConfig.getString("type"));
            boolean isExtended = potionConfig.getBoolean("extended");
            boolean isUpgraded = potionConfig.getBoolean("upgraded");

            try {
                PotionData potionData = new PotionData(potionType, isExtended, isUpgraded);
                itemBuilder.setPotion(potionData);
            }catch (NoClassDefFoundError d){

            }
        }
        if (config.isString("base64")) {
            itemBuilder.setSkullTexture(config.getString("base64"));
        }

        return itemBuilder;
    }

    public ItemBuilder withPlaceholders(Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }


}