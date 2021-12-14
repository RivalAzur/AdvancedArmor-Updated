package me.ilucah.advancedarmor.utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentUtils {

    public static Enchantment getEnchantment(String enchString) {
        enchString = enchString.toLowerCase().replaceAll("[ _-]", "");
        Map <String, String> aliases = new HashMap<String, String>();
        aliases.put("aspectfire", "fireaspect");
        aliases.put("sharpness", "damageall");
        aliases.put("smite", "damageundead");
        aliases.put("punch", "arrowknockback");
        aliases.put("looting", "lootbonusmobs");
        aliases.put("fortune", "lootbonusblocks");
        aliases.put("baneofarthropods", "damageundead");
        aliases.put("power", "arrowdamage");
        aliases.put("flame", "arrowfire");
        aliases.put("infinity", "arrowinfinite");
        aliases.put("unbreaking", "durability");
        aliases.put("efficiency", "digspeed");
        aliases.put("thorns", "thorns");
        aliases.put("projectile_protection", "protection_projectile");
        aliases.put("protection", "protection_environmental");
        aliases.put("blast_protection", "protection_explosions");
        aliases.put("fire_protection", "protection_fire");
        aliases.put("mending", "mending");
        String alias = aliases.get(enchString);
        if (alias != null)
            enchString = alias;
        for (Enchantment value : Enchantment.values()) {
            if (!enchString.contains("protection")) {
                if (enchString.equalsIgnoreCase(value.getName().replaceAll("[ _-]", ""))) {
                    return value;
                }
            } else {
                if (enchString.contains("environmental")){
                    return Enchantment.PROTECTION_ENVIRONMENTAL;
                } else if (enchString.contains("project")) {
                    return Enchantment.PROTECTION_PROJECTILE;
                } else if (enchString.contains("explosion") || (enchString.contains("blast"))) {
                    return Enchantment.PROTECTION_EXPLOSIONS;
                } else if (enchString.contains("fire")) {
                    return Enchantment.PROTECTION_FIRE;
                }
            }
        }
        return null;
    }

}