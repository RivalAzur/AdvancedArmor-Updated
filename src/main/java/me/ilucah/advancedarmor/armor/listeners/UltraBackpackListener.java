package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.boost.MoneyUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.drawethree.ultrabackpacks.api.event.BackpackSellEvent;

import java.text.DecimalFormat;

public class UltraBackpackListener implements Listener {

    private final AdvancedArmor plugin;
    private final MoneyUtils moneyUtils;
    private final DecimalFormat decimalFormat;

    public UltraBackpackListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.moneyUtils = new MoneyUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat( "###,###.00" );
    }

    @EventHandler
    public void onSell(BackpackSellEvent event) {
        final Player player = event.getPlayer();
        double amount = event.getMoneyToDeposit();

        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());

        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, moneyMulti, amount, BoostType.MONEY);
        plugin.getServer().getPluginManager().callEvent(boostEvent);

        event.setMoneyToDeposit((amount * moneyMulti) + boostEvent.getNewEarnings());

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

}
