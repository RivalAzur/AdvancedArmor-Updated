package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import net.brcdev.shopgui.shop.ShopManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class ShopGUIPlusListener implements Listener {

    private final AdvancedArmor plugin;

    public ShopGUIPlusListener(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopGUIPlusSell(ShopPreTransactionEvent event) {
        final Player player = event.getPlayer();
        final MoneyUtils moneyUtils = new MoneyUtils(plugin.getHandler());
        final DebugManager debugManager = new DebugManager(plugin);
        final MessageUtils messageUtils = new MessageUtils(plugin);
        final DecimalFormat decimalFormat = new DecimalFormat( "###,###.00" );

        if (event.getShopAction() == ShopManager.ShopAction.SELL ||
                event.getShopAction() == ShopManager.ShopAction.SELL_ALL) {
            double amountReceived = event.getPrice();
            double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());

            event.setPrice(amountReceived * moneyMulti);

            if (plugin.getConfig().getBoolean("Messages.BoostMessages.Money.Enabled")) {
                if (((amountReceived * moneyMulti) - amountReceived) != 0) {
                    messageUtils.getConfigMessage("BoostMessages.Money.Message").iterator().forEachRemaining(s -> {
                        if (s.contains("%amount%"))
                            s = s.replace("%amount%", String.valueOf((decimalFormat.format((amountReceived * moneyMulti) - amountReceived))));
                        player.sendMessage(s);
                    });
                }
            }

            if (debugManager.isEnabled()) {
                debugManager.moneyEventDebugInfoSend(amountReceived,
                        (amountReceived * moneyMulti) - amountReceived,
                        (amountReceived * moneyMulti), moneyMulti);
                debugManager.moneyEventDebugInfoSend(player, amountReceived,
                        (amountReceived * moneyMulti) - amountReceived,
                        (amountReceived * moneyMulti), moneyMulti);
            }
        }
    }
}

