package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.utilities.CoinUtils;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.quadrex.mobcoins.MobCoinAPI;
import org.quadrex.mobcoins.events.MobCoinEarnEvent;
import org.quadrex.mobcoins.storage.Database;

public class QuadrexMobCoinsListener implements Listener {

    private AdvancedArmor plugin;

    public QuadrexMobCoinsListener(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCoinReceive(MobCoinEarnEvent event) {
        Player player = event.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (!coinPlayer.hasCustomArmorEquipped())
            return;
        final CoinUtils coinUtils = new CoinUtils(plugin);
        final MessageUtils messageUtils = new MessageUtils(plugin);
        double amount = event.getAmount();

        double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        int amountToGive = (int) ((amount * coinMulti) - amount);

        addCoins(player, (long) amountToGive);

        if (plugin.getConfig().getBoolean("Messages.BoostMessages.Coins.Enabled")) {
            if ((amountToGive) > 0) {
                messageUtils.getConfigMessage("BoostMessages.Coins.Message").iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%")) {
                        int string = (int) (amountToGive);
                        if (string < 1)
                            string = 1;
                        s = s.replace("%amount%", Integer.toString(string));
                    }
                    player.sendMessage(s);
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
