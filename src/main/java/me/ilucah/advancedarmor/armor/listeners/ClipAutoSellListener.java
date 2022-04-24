package me.ilucah.advancedarmor.armor.listeners;

import me.clip.autosell.events.AutoSellEvent;
import me.clip.autosell.events.SellAllEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.boost.MoneyUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.text.DecimalFormat;

public class ClipAutoSellListener implements Listener {

    private final AdvancedArmor plugin;
    private final MoneyUtils moneyUtils;
    private final DecimalFormat decimalFormat;

    private Economy economy;

    public ClipAutoSellListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.moneyUtils = new MoneyUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat("###,###.00");

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
        double amountReceived = event.getPrice();
        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        double amountToGive = (amountReceived * moneyMulti) - amountReceived;
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, moneyMulti, event.getPrice(), BoostType.MONEY);
        plugin.getServer().getPluginManager().callEvent(boostEvent);

        economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), amountToGive + boostEvent.getNewEarnings());

        if (plugin.getHandler().getMessageManager().isMoneyIsEnabled()) {
            if (amountToGive > 0) {
                plugin.getHandler().getMessageManager().getMoneyMessage().iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%", String.valueOf((decimalFormat.format(boostEvent.getNewEarnings() + amountToGive))));
                    player.sendMessage(RGBParser.parse(s));
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

    @EventHandler
    public void onSellAll(SellAllEvent event) {
        final Player player = event.getPlayer();
        final MoneyUtils moneyUtils = new MoneyUtils(plugin.getHandler());
        final DecimalFormat decimalFormat = new DecimalFormat("###,###.00");

        double amountReceived = event.getTotalCost();
        double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        double amountToGive = (amountReceived * moneyMulti) - amountReceived;

        economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), amountToGive);

        if (plugin.getHandler().getMessageManager().isMoneyIsEnabled()) {
            if (amountToGive > 0) {
                plugin.getHandler().getMessageManager().getMoneyMessage().iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%", String.valueOf((decimalFormat.format(amountToGive))));
                    player.sendMessage(RGBParser.parse(s));
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
