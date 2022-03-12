package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
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
    private final MoneyUtils moneyUtils;
    private final DecimalFormat decimalFormat;

    public InfiniteChestProListener(AdvancedArmor plugin, IChestHookManager hookManager) {
        this.plugin = plugin;
        this.hookManager = hookManager;
        this.moneyUtils = new MoneyUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat("###,###.00");
    }

    @EventHandler
    public void onSell(InfiniteChestSellEvent event) {
        final OfflinePlayer offlinePlayer = event.getRecipient();
        double sellAmount = event.getFinalPrice();

        if (offlinePlayer.isOnline()) {
            final Player player = offlinePlayer.getPlayer();
            double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());

            hookManager.getEconomyHook().deposit(offlinePlayer, ((sellAmount * moneyMulti) - sellAmount));

            if (plugin.getHandler().getMessageManager().isMoneyIsEnabled()) {
                if (((sellAmount * moneyMulti) - sellAmount) != 0) {
                    plugin.getHandler().getMessageManager().getMoneyMessage().iterator().forEachRemaining(s -> {
                        if (s.contains("%amount%"))
                            s = s.replace("%amount%", String.valueOf(decimalFormat.format(((sellAmount * moneyMulti) - sellAmount))));
                        player.sendMessage(RGBParser.parse(s));
                    });
                }
            }

            if (plugin.getHandler().getDebugManager().isEnabled()) {
                plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(sellAmount,
                        (sellAmount * moneyMulti) - sellAmount,
                        (sellAmount * moneyMulti), moneyMulti);
                plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(player, sellAmount,
                        (sellAmount * moneyMulti) - sellAmount,
                        (sellAmount * moneyMulti), moneyMulti);
            }
        }
    }
}
