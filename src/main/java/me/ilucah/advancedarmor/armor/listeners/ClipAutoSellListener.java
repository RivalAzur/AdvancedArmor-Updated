package me.ilucah.advancedarmor.armor.listeners;

import me.clip.autosell.events.AutoSellEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.text.DecimalFormat;

public class ClipAutoSellListener implements Listener {

    private AdvancedArmor plugin;

    private Economy economy;

    public ClipAutoSellListener(AdvancedArmor plugin) {
        this.plugin = plugin;

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning("Failed to hook into AutoSell. Vault provider was unsuccessfully hooked. Money Component disabled. This may cause issues.");
            return;
        }
        this.economy = rsp.getProvider();
    }

    @EventHandler
    public void onSell(AutoSellEvent event) {
        final Player player = event.getPlayer();
        final MoneyUtils moneyUtils = new MoneyUtils(plugin.getHandler());
        final MessageUtils messageUtils = new MessageUtils(plugin);
        final DecimalFormat decimalFormat = new DecimalFormat( "###,###.00" );

        double amountReceived = event.getPrice();
        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        double amountToGive = (amountReceived * moneyMulti) - amountReceived;

        economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), amountToGive);

        if (plugin.getConfig().getBoolean("Messages.BoostMessages.Money.Enabled")) {
            if (amountToGive > 0) {
                messageUtils.getConfigMessage("BoostMessages.Money.Message").iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%", String.valueOf((decimalFormat.format(amountToGive))));
                    player.sendMessage(s);
                });
            }
        }

        if (plugin.getHandler().getDebugManager().isEnabled()) {
            plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(amountReceived,
                    (amountReceived * moneyMulti) - amountReceived,
                    (amountReceived * moneyMulti), moneyMulti);
            plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(player, amountReceived,
                    (amountReceived * moneyMulti) - amountReceived,
                    (amountReceived * moneyMulti), moneyMulti);
        }
    }
}
