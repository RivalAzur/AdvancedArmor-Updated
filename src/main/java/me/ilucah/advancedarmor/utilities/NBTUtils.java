package me.ilucah.advancedarmor.utilities;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.ilucah.advancedarmor.handler.Handler;
import org.bukkit.inventory.ItemStack;

public class NBTUtils {

    private final Handler handler;

    public NBTUtils(Handler handler) {
        this.handler = handler;
    }

    public boolean hasArmorNBTTag(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasKey("CustomArmor"))
            return true;
        return false;
    }

    public String getArmorName(ItemStack item) {
        return new NBTItem(item).getString("CustomArmor");
    }
}
