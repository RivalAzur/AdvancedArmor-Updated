package me.ilucah.advancedarmor.armor.listeners;

import me.aglerr.krakenmobcoins.MobCoins;
import me.aglerr.krakenmobcoins.api.events.MobCoinsReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.utilities.CoinUtils;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KrakenMobCoinsListener implements Listener {

    private final AdvancedArmor plugin;
    private final CoinUtils coinUtils;

    public KrakenMobCoinsListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.coinUtils = new CoinUtils(plugin);
    }

    @EventHandler
    public void onMobCoinsReceive(MobCoinsReceiveEvent event) {
        final Player player = event.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (coinPlayer.hasCustomArmorEquipped()) {
            final double amount = event.getAmountBeforeMultiplier();
            double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());
            int amountToGive = (int) ((amount * coinMulti) - amount);
            MobCoins.getAPI().getSalaryManager().setPlayerSalary(player.getUniqueId(), MobCoins.getAPI()
                    .getSalaryManager().getPlayerSalary(player.getUniqueId()) + amountToGive);

            if (plugin.getHandler().getMessageManager().isCoinIsEnabled()) {
                if (((amount * coinMulti) - amount) != 0) {
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
