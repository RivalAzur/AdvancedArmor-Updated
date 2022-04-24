package me.ilucah.advancedarmor.armor.listeners;

import dev.norska.dsw.api.DeluxeSellwandSellEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.boost.MoneyUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class DeluxSellWandsMoneyListener implements Listener {

    private final AdvancedArmor plugin;
    private final MoneyUtils moneyUtils;
    private final DecimalFormat decimalFormat;

    public DeluxSellWandsMoneyListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.moneyUtils = new MoneyUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat("###,###.00");
    }

    @EventHandler
    public void onSell(DeluxeSellwandSellEvent event) {
        System.out.println("money sell event");
        final Player player = event.getPlayer();
        double amountReceived = event.getMoney();
        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, moneyMulti, amountReceived, BoostType.MONEY);
        plugin.getServer().getPluginManager().callEvent(boostEvent);

        event.setMoney((amountReceived * moneyMulti) + boostEvent.getNewEarnings());
        System.out.println("Old amount: " + amountReceived);
        System.out.println("money multi: " + moneyMulti);
        System.out.println("new amount: " + amountReceived * moneyMulti);

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
}
