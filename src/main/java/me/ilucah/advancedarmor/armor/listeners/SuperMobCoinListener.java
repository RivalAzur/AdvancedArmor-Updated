package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.CoinPlayer;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.CoinUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import me.swanis.mobcoins.events.MobCoinsReceiveEvent;
import me.swanis.mobcoins.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperMobCoinListener implements Listener {

    private final AdvancedArmor plugin;
    private final CoinUtils coinUtils;

    public SuperMobCoinListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.coinUtils = new CoinUtils(plugin);
    }

    @EventHandler
    public void onMobCoinReceive(MobCoinsReceiveEvent event) {
        final Profile profile = event.getProfile();
        final Player player = profile.getPlayer();
        final CoinPlayer coinPlayer = new CoinPlayer(plugin.getHandler(), player);
        if (coinPlayer.hasCustomArmorEquipped()) {
            double amount = event.getAmount();

            double coinMulti = coinUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());
            int amountToGive = (int) ((amount * coinMulti) - amount);
            ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, coinMulti, amount, BoostType.COIN);
            plugin.getServer().getPluginManager().callEvent(boostEvent);

            profile.setMobCoins(profile.getMobCoins() + amountToGive + (long) boostEvent.getNewEarnings());

            if (plugin.getHandler().getMessageManager().isCoinIsEnabled()) {
                if (amountToGive != 0) {
                    plugin.getHandler().getMessageManager().getCoinMessage().iterator().forEachRemaining(s -> {
                        if (s.contains("%amount%")) {
                            int string = (int) ((amount * coinMulti) - amount + (int) boostEvent.getNewEarnings());
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
                player.sendMessage("SetAmount" + ((profile.getMobCoins() + amountToGive) - amount));
            }
        }
    }
}