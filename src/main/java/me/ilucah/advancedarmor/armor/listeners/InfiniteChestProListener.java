package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import me.ilucah.advancedarmor.utilities.ichest.IChestHookManager;
import net.luckyfeed.events.InfiniteChestSellEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class InfiniteChestProListener implements Listener {

    private final AdvancedArmor plugin;
    private final IChestHookManager hookManager;

    public InfiniteChestProListener(AdvancedArmor plugin, IChestHookManager hookManager) {
        this.plugin = plugin;
        this.hookManager = hookManager;
    }

    @EventHandler
    public void onSell(InfiniteChestSellEvent event) {
        final OfflinePlayer offlinePlayer = event.getRecipient();
        final MoneyUtils moneyUtils = new MoneyUtils(plugin.getHandler());
        final DebugManager debugManager = new DebugManager(plugin);
        final MessageUtils messageUtils = new MessageUtils(plugin);
        final DecimalFormat decimalFormat = new DecimalFormat( "###,###.00" );
        double sellAmount = event.getFinalPrice();

        if (offlinePlayer.isOnline()) {
            final Player player = offlinePlayer.getPlayer();
            double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());

            hookManager.getEconomyHook().deposit(offlinePlayer, ((sellAmount * moneyMulti) - sellAmount));

            if (plugin.getConfig().getBoolean("Messages.BoostMessages.Money.Enabled")) {
                if (((sellAmount * moneyMulti) - sellAmount) != 0) {
                    messageUtils.getConfigMessage("BoostMessages.Money.Message").iterator().forEachRemaining(s -> {
                        if (s.contains("%amount%"))
                            s = s.replace("%amount%", String.valueOf(decimalFormat.format(((sellAmount * moneyMulti) - sellAmount))));
                        player.sendMessage(s);
                    });
                }
            }

            if (debugManager.isEnabled()) {
                debugManager.moneyEventDebugInfoSend(sellAmount,
                        (sellAmount * moneyMulti) - sellAmount,
                        (sellAmount * moneyMulti), moneyMulti);
                debugManager.moneyEventDebugInfoSend(player, sellAmount,
                        (sellAmount * moneyMulti) - sellAmount,
                        (sellAmount * moneyMulti), moneyMulti);
            }
        }
    }
}
