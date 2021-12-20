package me.ilucah.advancedarmor.armor.listeners;

import me.drawethree.ultraprisoncore.autosell.api.events.UltraPrisonAutoSellEvent;
import me.drawethree.ultraprisoncore.autosell.api.events.UltraPrisonSellAllEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class UltraPrisonCoreListener implements Listener {

    private final AdvancedArmor plugin;

    public UltraPrisonCoreListener(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSellAll(UltraPrisonSellAllEvent event) {
        final Player player = event.getPlayer();
        final MoneyUtils moneyUtils = new MoneyUtils(plugin.getHandler());
        final DebugManager debugManager = new DebugManager(plugin);
        final MessageUtils messageUtils = new MessageUtils(plugin);
        final DecimalFormat decimalFormat = new DecimalFormat( "###,###.00" );
        double amount = event.getSellPrice();

        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());

        event.setSellPrice(amount * moneyMulti);

        if (plugin.getConfig().getBoolean("Messages.BoostMessages.Money.Enabled")) {
            if (((amount * moneyMulti) - amount) != 0) {
                messageUtils.getConfigMessage("BoostMessages.Money.Message").iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%",(String.valueOf(decimalFormat.format((amount * moneyMulti) - amount))));
                    player.sendMessage(s);
                });
            }
        }

        if (debugManager.isEnabled()) {
            debugManager.moneyEventDebugInfoSend(amount,
                    (amount * moneyMulti) - amount,
                    (amount * moneyMulti), moneyMulti);
            debugManager.moneyEventDebugInfoSend(player, amount,
                    (amount * moneyMulti) - amount,
                    (amount * moneyMulti), moneyMulti);
        }
    }

    @EventHandler
    public void onAutoSell(UltraPrisonAutoSellEvent event) {
        final Player player = event.getPlayer();
        final MoneyUtils moneyUtils = new MoneyUtils(plugin.getHandler());
        final DebugManager debugManager = new DebugManager(plugin);
        final MessageUtils messageUtils = new MessageUtils(plugin);
        final DecimalFormat decimalFormat = new DecimalFormat( "###,###.00" );
        double amount = event.getMoneyToDeposit();

        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());

        event.setMoneyToDeposit(amount * moneyMulti);

        if (plugin.getConfig().getBoolean("Messages.BoostMessages.Money.Enabled")) {
            if (((amount * moneyMulti) - amount) != 0) {
                messageUtils.getConfigMessage("BoostMessages.Money.Message").iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%", decimalFormat.format(String.valueOf((amount * moneyMulti) - amount)));
                    player.sendMessage(s);
                });
            }
        }

        if (debugManager.isEnabled()) {
            debugManager.moneyEventDebugInfoSend(amount,
                    (amount * moneyMulti) - amount,
                    (amount * moneyMulti), moneyMulti);
            debugManager.moneyEventDebugInfoSend(player, amount,
                    (amount * moneyMulti) - amount,
                    (amount * moneyMulti), moneyMulti);
        }
    }
}
