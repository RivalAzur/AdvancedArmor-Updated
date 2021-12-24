package me.ilucah.advancedarmor.armor;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Flag {

    public static void addItemFlags(List<Flag> itemFlags, ItemMeta meta) {
        itemFlags.iterator().forEachRemaining(itemFlag -> {
            if (itemFlag.isUnbreakable())
                meta.setUnbreakable(true);
            else
                meta.addItemFlags(itemFlag.getItemFlag());
        });
    }

    private boolean isUnbreakable;
    private ItemFlag itemFlag;

    public Flag(ItemFlag itemFlag, boolean unbreakable) {
        isUnbreakable = false;
        if (itemFlag == null)
            isUnbreakable = true;
        else
            this.itemFlag = itemFlag;
    }

    public boolean isUnbreakable() {
        return isUnbreakable;
    }

    public ItemFlag getItemFlag() {
        return this.itemFlag;
    }
}
