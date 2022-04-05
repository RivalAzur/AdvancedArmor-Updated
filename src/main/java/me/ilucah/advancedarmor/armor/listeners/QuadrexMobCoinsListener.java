package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.CoinUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.quadrex.mobcoins.MobCoinAPI;
import org.quadrex.mobcoins.events.MobCoinEarnEvent;
import org.quadrex.mobcoins.storage.Database;

public class QuadrexMobCoinsListener implements Listener {

    private final AdvancedArmor plugin;
    private final CoinUtils coinUtils;

    public QuadrexMobCoinsListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.coinUtils = new CoinUtils(plugin);
    }

    @EventHandler
    public void onCoinReceive(MobCoinEarnEvent event) {
        Player player = event.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (!coinPlayer.hasCustomArmorEquipped())
            return;
        double amount = event.getAmount();

        double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        int amountToGive = (int) ((amount * coinMulti) - amount);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, coinMulti, amount, BoostType.COIN);
        plugin.getServer().getPluginManager().callEvent(boostEvent);

        addCoins(player, amountToGive + (int) boostEvent.getNewEarnings());

        if (plugin.getHandler().getMessageManager().isCoinIsEnabled()) {
            if ((amountToGive) > 0) {
                plugin.getHandler().getMessageManager().getCoinMessage().iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%")) {
                        int string = amountToGive + (int) boostEvent.getNewEarnings();
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
            player.sendMessage("SetAmount" + ((MobCoinAPI.getInstance().getBalance(player) + amountToGive) - amount));
        }
    }

    private void addCoins(Player p, long amount) {
        long balance = Database.getInstance().getBalance(p.getUniqueId());
        balance += amount;
        Database.getInstance().setBalance(p.getUniqueId(), balance);
    }
}
