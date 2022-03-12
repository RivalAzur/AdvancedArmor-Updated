package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import net.brcdev.shopgui.shop.ShopManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class ShopGUIPlusListener implements Listener {

    private final AdvancedArmor plugin;
    private final MoneyUtils moneyUtils;
    private final DecimalFormat decimalFormat;

    public ShopGUIPlusListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.moneyUtils = new MoneyUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat( "###,###.00" );
    }

    @EventHandler
    public void onShopGUIPlusSell(ShopPreTransactionEvent event) {
        final Player player = event.getPlayer();

        if (event.getShopAction() == ShopManager.ShopAction.SELL ||
                event.getShopAction() == ShopManager.ShopAction.SELL_ALL) {
            double amountReceived = event.getPrice();
            double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());

            event.setPrice(amountReceived * moneyMulti);

            if (plugin.getHandler().getMessageManager().isMoneyIsEnabled()) {
                if (((amountReceived * moneyMulti) - amountReceived) != 0) {
                    plugin.getHandler().getMessageManager().getMoneyMessage().iterator().forEachRemaining(s -> {
                        if (s.contains("%amount%"))
                            s = s.replace("%amount%", String.valueOf((decimalFormat.format((amountReceived * moneyMulti) - amountReceived))));
                        player.sendMessage(RGBParser.parse(s));
                    });
                }
            }

            if (plugin.getHandler().getDebugManager().isEnabled()) {
                plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(amountReceived,
                        (amountReceived * moneyMulti) - amountReceived,
                        (amountReceived * moneyMulti), moneyMulti);
                plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(player, amountReceived,
                        (amountReceived * moneyMulti) - amountReceived,
                        (amountReceived * moneyMulti), moneyMulti);
            }
        }
    }
}

