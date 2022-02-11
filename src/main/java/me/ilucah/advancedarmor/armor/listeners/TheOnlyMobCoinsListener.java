package me.ilucah.advancedarmor.armor.listeners;

import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.api.events.MobCoinsReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.utilities.CoinUtils;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;

public class TheOnlyMobCoinsListener implements Listener {

    private final AdvancedArmor plugin;

    public TheOnlyMobCoinsListener(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobCoinReceiveEvent(MobCoinsReceiveEvent event) {
        final Player player = event.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (coinPlayer.hasCustomArmorEquipped()) {
            if (MobCoinsAPI.getPlayerData(player) != null) {
                final PlayerData playerData = MobCoinsAPI.getPlayerData(player);
                final CoinUtils coinUtils = new CoinUtils(plugin);
                final DebugManager debugManager = new DebugManager(plugin);
                final MessageUtils messageUtils = new MessageUtils(plugin);
                final double amount = event.getAmountReceived();
                double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                        player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                        player.getInventory().getBoots());
                int amountToGive = (int) ((amount * coinMulti) - amount);
                playerData.addCoins(playerData.getCoins() + amountToGive);
                // MobCoins.getAPI().getSalaryManager().setPlayerSalary(player.getUniqueId(), MobCoins.getAPI()
                //        .getSalaryManager().getPlayerSalary(player.getUniqueId()) + amountToGive);

                if (plugin.getConfig().getBoolean("Messages.BoostMessages.Coins.Enabled")) {
                    if (((amount * coinMulti) - amount) != 0) {
                        messageUtils.getConfigMessage("BoostMessages.Coins.Message").iterator().forEachRemaining(s -> {
                            if (s.contains("%amount%")) {
                                int string = (int) ((amount * coinMulti) - amount);
                                s = s.replace("%amount%", Integer.toString(string));
                            }
                            player.sendMessage(s);
                        });
                    }
                }

                if (debugManager.isEnabled()) {
                    player.sendMessage("Amount" + amount);
                    player.sendMessage("Multi" + coinMulti);
                    player.sendMessage("AmountToGive" + amountToGive);
                }
            }
        }
    }
}
