package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.armor.Flag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FlagObjective {

    private List<Flag> flags;
    private boolean applyUnbreaking;

    public FlagObjective(List<Flag> flags) {
        this.flags = flags;
    }

    public void applyFlags(ItemMeta meta) {
        applyUnbreaking = Flag.addItemFlags(flags, meta);
    }

    public boolean isApplyUnbreaking() {
        return applyUnbreaking;
    }

    public void setApplyUnbreaking(boolean applyUnbreaking) {
        this.applyUnbreaking = applyUnbreaking;
    }
}
