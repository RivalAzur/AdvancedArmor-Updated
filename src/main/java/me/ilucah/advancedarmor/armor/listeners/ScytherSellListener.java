package me.ilucah.advancedarmor.armor.listeners;

import dev.norska.scyther.api.ScytherAutosellEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.RGBParser;
import me.ilucah.advancedarmor.utilities.boost.ExpUtils;
import me.ilucah.advancedarmor.utilities.boost.MoneyUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class ScytherSellListener implements Listener {

    private final AdvancedArmor plugin;
    private final MoneyUtils moneyUtils;
    private final ExpUtils expUtils;
    private final DecimalFormat decimalFormat;

    public ScytherSellListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.moneyUtils = new MoneyUtils(plugin.getHandler());
        this.expUtils = new ExpUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat("###,###.00");
    }

    @EventHandler
    public void onMoneySell(ScytherAutosellEvent event) {
        final Player player = event.getPlayer();
        double amountReceived = event.getMoney();
        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, moneyMulti, amountReceived, BoostType.MONEY);
        plugin.getServer().getPluginManager().callEvent(boostEvent);

        event.setMoney((amountReceived * moneyMulti) + boostEvent.getNewEarnings());
        if (plugin.getHandler().getMessageManager().isMoneyIsEnabled()) {
            if (((amountReceived * moneyMulti) - amountReceived) != 0) {
                plugin.getHandler().getMessageManager().getMoneyMessage().iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%", String.valueOf((decimalFormat.format((amountReceived * moneyMulti) - amountReceived + boostEvent.getNewEarnings()))));
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

    @EventHandler
    public void onExpSell(ScytherAutosellEvent event) {
        final Player player = event.getPlayer();

        double expMulti = expUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, expMulti, event.getAmount(), BoostType.EXP);
        Bukkit.getPluginManager().callEvent(boostEvent);
        player.giveExp((int) ((event.getAmount() * expMulti) - event.getAmount() + boostEvent.getNewEarnings()));

        if (plugin.getHandler().getMessageManager().isExpIsEnabled()) {
            if (((int) ((event.getAmount() * expMulti) - event.getAmount())) != 0) {
                plugin.getHandler().getMessageManager().getExpMessage().iterator().forEachRemaining(s -> {
                        s = s.replace("%amount%", decimalFormat.format((int) ((event.getAmount() * expMulti) - event.getAmount() + boostEvent.getNewEarnings())));
                    player.sendMessage(RGBParser.parse(s));
                });
            }
        }

        if (plugin.getHandler().getDebugManager().isEnabled()) {
            plugin.getHandler().getDebugManager().expEventDebugInfoSend(event.getAmount(),
                    (int) ((event.getAmount() * expMulti) - event.getAmount()),
                    (int) ((event.getAmount() * expMulti)), expMulti);
            plugin.getHandler().getDebugManager().expEventDebugInfoSend(player, event.getAmount(),
                    (int) ((event.getAmount() * expMulti) - event.getAmount()),
                    (int) ((event.getAmount() * expMulti)), expMulti);
        }
    }
}
