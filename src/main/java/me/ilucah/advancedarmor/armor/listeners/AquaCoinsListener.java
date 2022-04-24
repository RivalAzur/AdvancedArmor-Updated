package me.ilucah.advancedarmor.armor.listeners;

import me.aqua.aquacoins.api.CoinReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.boost.CoinUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AquaCoinsListener implements Listener {

    private final AdvancedArmor plugin;
    private final CoinUtils coinUtils;

    public AquaCoinsListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.coinUtils = new CoinUtils(plugin);
    }

    @EventHandler
    public void onReceive(CoinReceiveEvent event) {
        Player player = event.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (!coinPlayer.hasCustomArmorEquipped())
            return;
        long amount = event.getCoins();
        double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        long amountToGive = (long) ((amount * coinMulti) - amount);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, coinMulti, amount, BoostType.COIN);
        plugin.getServer().getPluginManager().callEvent(boostEvent);
        event.setCoins(amount + amountToGive + ((long) boostEvent.getNewEarnings()));
        if (plugin.getHandler().getMessageManager().isCoinIsEnabled()) {
            if ((amountToGive) > 0) {
                plugin.getHandler().getMessageManager().getCoinMessage().iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%")) {
                        int string = (int) (amountToGive + boostEvent.getNewEarnings());
                        if (string < 1)
                            string = 1;
                        s = s.replace("%amount%", Integer.toString(string));
                    }
                    player.sendMessage(RGBParser.parse(s));
                });
            }
        }
        if (plugin.getHandler().getDebugManager().isEnabled()) {
            player.sendMessage("Amount" + amount);
            player.sendMessage("Multi" + coinMulti);
            player.sendMessage("AmountToGive" + amountToGive);
        }
    }

}