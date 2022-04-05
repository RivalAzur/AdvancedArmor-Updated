package me.ilucah.advancedarmor.armor.listeners;

import dev.drawethree.ultraprisoncore.UltraPrisonCore;
import dev.drawethree.ultraprisoncore.api.enums.ReceiveCause;
import dev.drawethree.ultraprisoncore.tokens.api.events.PlayerTokensReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.TokenPlayer;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.RGBParser;
import me.ilucah.advancedarmor.utilities.TokenUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UPCTokenListener implements Listener {

    private AdvancedArmor plugin;
    private TokenUtils tokenUtils;

    public UPCTokenListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.tokenUtils = new TokenUtils(plugin);
    }

    @EventHandler
    public void onTokenRecieveEvent(PlayerTokensReceiveEvent event) {
        if (event.getCause() == ReceiveCause.MINING ||
                event.getCause() == ReceiveCause.LUCKY_BLOCK ||
                event.getCause() == ReceiveCause.MINING_OTHERS) {
            final Player player = event.getPlayer().getPlayer();
            final TokenPlayer tokenPlayer = new TokenPlayer(plugin.getHandler(), player);
            if (!tokenPlayer.hasCustomArmorEquipped())
                return;
            long amount = event.getAmount();

            double coinMulti = tokenUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());
            long amountToGive = (long) ((amount * coinMulti) - amount);
            ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, coinMulti, amount, BoostType.TOKEN);
            plugin.getServer().getPluginManager().callEvent(boostEvent);

            UltraPrisonCore.getInstance().getTokens().getApi().addTokens(event.getPlayer(), amountToGive + (long) boostEvent.getNewEarnings(), ReceiveCause.GIVE);

            if (plugin.getHandler().getMessageManager().isTokenIsEnabled()) {
                if (((amount * coinMulti) - amount) != 0) {
                   plugin.getHandler().getMessageManager().getTokenMessage().iterator().forEachRemaining(s -> {
                        if (s.contains("%amount%")) {
                            int string = (int) ((amount * coinMulti) - amount) + (int) boostEvent.getNewEarnings();
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
