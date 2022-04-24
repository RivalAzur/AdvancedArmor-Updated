package me.ilucah.advancedarmor.armor.listeners;

import dev.norska.dsw.api.DeluxeSellwandTokenReceiveEvent;
import dev.norska.dsw.api.DeluxeSellwandsAPI;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.RGBParser;
import me.ilucah.advancedarmor.utilities.boost.TokenUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class DeluxSellWandsTokenListener implements Listener {

    private final AdvancedArmor plugin;
    private final TokenUtils moneyUtils;
    private final DecimalFormat decimalFormat;

    public DeluxSellWandsTokenListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.moneyUtils = new TokenUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat("#,###");
    }

    @EventHandler
    public void onSell(DeluxeSellwandTokenReceiveEvent event) {
        final Player player = event.getPlayer();
        int amountReceived = event.getTokens();
        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, moneyMulti, amountReceived, BoostType.TOKEN);
        plugin.getServer().getPluginManager().callEvent(boostEvent);
        DeluxeSellwandsAPI.addPlayerTokens(player.getUniqueId(), (int) (((amountReceived * moneyMulti) - amountReceived) + boostEvent.getNewEarnings()));

        if (plugin.getHandler().getMessageManager().isTokenIsEnabled()) {
            if (((amountReceived * moneyMulti) - amountReceived) != 0) {
                plugin.getHandler().getMessageManager().getTokenMessage().iterator().forEachRemaining(s -> {
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
