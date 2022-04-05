package me.ilucah.advancedarmor.armor.listeners;

import dev.drawethree.ultraprisoncore.autosell.api.events.UltraPrisonAutoSellEvent;
import dev.drawethree.ultraprisoncore.autosell.api.events.UltraPrisonSellAllEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class UltraPrisonCoreListener implements Listener {

    private final AdvancedArmor plugin;
    private final MoneyUtils moneyUtils;
    private final DecimalFormat decimalFormat;

    public UltraPrisonCoreListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.moneyUtils = new MoneyUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat( "###,###.00" );
    }

    @EventHandler
    public void onSellAll(UltraPrisonSellAllEvent event) {
        final Player player = event.getPlayer();
        double amount = event.getSellPrice();

        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, moneyMulti, amount, BoostType.MONEY);
        plugin.getServer().getPluginManager().callEvent(boostEvent);

        event.setSellPrice(amount * moneyMulti + boostEvent.getNewEarnings());

        if (plugin.getHandler().getMessageManager().isMoneyIsEnabled()) {
            if (((amount * moneyMulti) - amount) != 0) {
                plugin.getHandler().getMessageManager().getMoneyMessage().iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%",(String.valueOf(decimalFormat.format((amount * moneyMulti) - amount + boostEvent.getNewEarnings()))));
                    player.sendMessage(RGBParser.parse(s));
                });
            }
        }

        if (plugin.getHandler().getDebugManager().isEnabled()) {
            plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(amount,
                    (amount * moneyMulti) - amount,
                    (amount * moneyMulti), moneyMulti);
            plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(player, amount,
                    (amount * moneyMulti) - amount,
                    (amount * moneyMulti), moneyMulti);
        }
    }

    @EventHandler
    public void onAutoSell(UltraPrisonAutoSellEvent event) {
        final Player player = event.getPlayer();
        double amount = event.getMoneyToDeposit();

        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());

        event.setMoneyToDeposit(amount * moneyMulti);

        if (plugin.getHandler().getMessageManager().isMoneyIsEnabled()) {
            if (((amount * moneyMulti) - amount) != 0) {
                plugin.getHandler().getMessageManager().getMoneyMessage().iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%", decimalFormat.format((amount * moneyMulti) - amount));
                    player.sendMessage(RGBParser.parse(s));
                });
            }
        }

        if (plugin.getHandler().getDebugManager().isEnabled()) {
            plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(amount,
                    (amount * moneyMulti) - amount,
                    (amount * moneyMulti), moneyMulti);
            plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(player, amount,
                    (amount * moneyMulti) - amount,
                    (amount * moneyMulti), moneyMulti);
        }
    }
}
