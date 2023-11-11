package me.ilucah.advancedarmor.armor;

import de.tr7zw.nbtapi.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Flag {

    public static boolean addItemFlags(List<Flag> itemFlags, ItemMeta meta) {
        boolean applyUnbreaking = false;
        for (Flag itemFlag : itemFlags) {
            if (itemFlag.isUnbreakable()) {
                applyUnbreaking = true;
            } else {
                meta.addItemFlags(itemFlag.getItemFlag());
            }
        }
        return applyUnbreaking;
    }

    public static void setUnbreakable(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item, true);
        nbtItem.setInteger("Unbreakable", 1);
    }

    private boolean isUnbreakable;
    private ItemFlag itemFlag;

    public Flag(ItemFlag itemFlag, boolean unbreakable) {
        isUnbreakable = false;
        if (itemFlag == null) isUnbreakable = true;
        else this.itemFlag = itemFlag;
    }

    public boolean isUnbreakable() {
        return isUnbreakable;
    }

    public ItemFlag getItemFlag() {
        return this.itemFlag;
    }

}
