package me.ilucah.advancedarmor.utilities.ichest;

import com.earth2me.essentials.api.Economy;
import me.ilucah.advancedarmor.AdvancedArmor;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;

public class IEssentialsHook extends IHook {

    private final AdvancedArmor plugin;

    public IEssentialsHook(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    @Override
    public void deposit(OfflinePlayer paramOfflinePlayer, double paramAmount) {
        try {
            Economy.add(paramOfflinePlayer.getUniqueId(), new BigDecimal(paramAmount));
        } catch (Exception e) {
            plugin.getLogger().warning("InfiniteChestPro Hook failed to deposit from essentials dependency");
        }
    }
}
