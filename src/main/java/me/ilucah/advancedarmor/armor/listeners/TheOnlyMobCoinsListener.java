package me.ilucah.advancedarmor.armor.listeners;

import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.api.events.MobCoinsReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.CoinUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;

public class TheOnlyMobCoinsListener implements Listener {

    private final AdvancedArmor plugin;
    private final CoinUtils coinUtils;

    public TheOnlyMobCoinsListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.coinUtils = new CoinUtils(plugin);
    }

    @EventHandler
    public void onMobCoinReceiveEvent(MobCoinsReceiveEvent event) {
        final Player player = event.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (coinPlayer.hasCustomArmorEquipped()) {
            if (MobCoinsAPI.getPlayerData(player) != null) {
                final PlayerData playerData = MobCoinsAPI.getPlayerData(player);
                final double amount = event.getAmountReceived();
                double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                        player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                        player.getInventory().getBoots());
                int amountToGive = (int) ((amount * coinMulti) - amount);
                ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, amountToGive, BoostType.COIN);
                plugin.getServer().getPluginManager().callEvent(boostEvent);
                playerData.addCoins(playerData.getCoins() + amountToGive);

                if (plugin.getHandler().getMessageManager().isCoinIsEnabled()) {
                    if (amountToGive != 0) {
                        plugin.getHandler().getMessageManager().getCoinMessage().iterator().forEachRemaining(s -> {
                            if (s.contains("%amount%")) {
                                int string = (int) ((amount * coinMulti) - amount);
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
    }
}
