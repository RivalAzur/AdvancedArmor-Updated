package me.ilucah.advancedarmor.armor.listeners;

import me.drawethree.ultraprisoncore.UltraPrisonCore;
import me.drawethree.ultraprisoncore.api.enums.ReceiveCause;
import me.drawethree.ultraprisoncore.tokens.api.events.PlayerTokensReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.handler.apimanager.TokenPlayer;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.TokenUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UPCTokenListener implements Listener {

    private AdvancedArmor plugin;

    public UPCTokenListener(AdvancedArmor plugin) {
        this.plugin = plugin;
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

            final TokenUtils tokenUtils = new TokenUtils(plugin);
            final MessageUtils messageUtils = new MessageUtils(plugin);
            long amount = event.getAmount();

            double coinMulti = tokenUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());
            long amountToGive = (long) ((amount * coinMulti) - amount);

            UltraPrisonCore.getInstance().getTokens().getApi().addTokens(event.getPlayer(), amountToGive, ReceiveCause.GIVE);

            if (plugin.getConfig().getBoolean("Messages.BoostMessages.Tokens.Enabled")) {
                if (((amount * coinMulti) - amount) != 0) {
                    messageUtils.getConfigMessage("BoostMessages.Tokens.Message").iterator().forEachRemaining(s -> {
                        if (s.contains("%amount%")) {
                            int string = (int) ((amount * coinMulti) - amount);
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
            }
        }
    }
}
