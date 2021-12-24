package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.utilities.CoinUtils;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.swanis.mobcoins.events.MobCoinsReceiveEvent;
import me.swanis.mobcoins.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperMobCoinListener implements Listener {

    private final AdvancedArmor plugin;

    public SuperMobCoinListener(AdvancedArmor plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobCoinReceive(MobCoinsReceiveEvent event) {
        final Profile profile = event.getProfile();
        final Player player = profile.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (coinPlayer.hasCustomArmorEquipped()) {
            final CoinUtils coinUtils = new CoinUtils(plugin);
            final DebugManager debugManager = new DebugManager(plugin);
            final MessageUtils messageUtils = new MessageUtils(plugin);
            double amount = event.getAmount();

            double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());
            int amountToGive = ((int) (amount * coinMulti)) -1;
            if (amountToGive < 0) {
                amountToGive = 0;
            }
            profile.setMobCoins(profile.getMobCoins() + amountToGive);
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
                debugManager.moneyEventDebugInfoSend(amount,
                        (amount * coinMulti) - amount,
                        (amount * coinMulti), coinMulti);
                debugManager.moneyEventDebugInfoSend(player, amount,
                        (amount * coinMulti) - amount,
                        (amount * coinMulti), coinMulti);
            }
        }
    }
}