package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.boost.CoinUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import net.devtm.tmmobcoins.API.MobCoinReceiveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TMMobcoinsListener implements Listener {

    private final AdvancedArmor plugin;
    private final CoinUtils coinUtils;

    public TMMobcoinsListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.coinUtils = new CoinUtils(plugin);
    }

    @EventHandler
    public void onReceive(MobCoinReceiveEvent event) {
        Player player = event.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (!coinPlayer.hasCustomArmorEquipped())
            return;
        double amount = event.getObtainedAmount();

        double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        int amountToGive = (int) ((amount * coinMulti) - amount);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, coinMulti, amount, BoostType.COIN);
        plugin.getServer().getPluginManager().callEvent(boostEvent);

        event.setDropAmount(amount + amountToGive + (boostEvent.getNewEarnings()));

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
        }
    }
}
