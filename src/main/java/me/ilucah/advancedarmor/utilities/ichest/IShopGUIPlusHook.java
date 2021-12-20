package me.ilucah.advancedarmor.utilities.ichest;

import me.ilucah.advancedarmor.AdvancedArmor;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.OfflinePlayer;

public class IShopGUIPlusHook extends IHook {

    private final AdvancedArmor plugin;

    public IShopGUIPlusHook(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    @Override
    public void deposit(OfflinePlayer paramOfflinePlayer, double paramAmount) {
        ShopGuiPlusApi.getPlugin().getEconomyManager().getDefaultEconomyProvider()
                .deposit(paramOfflinePlayer.getPlayer(), paramAmount);
    }
}
