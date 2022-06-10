package me.ilucah.advancedarmor.boosting.providers;

import me.clip.autosell.events.AutoSellEvent;
import me.clip.autosell.events.SellAllEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BiBoostProvider;
import me.ilucah.advancedarmor.api.events.ArmorBoostGiveEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.text.DecimalFormat;

public class ClipAutosellProvider extends BiBoostProvider<SellAllEvent, AutoSellEvent> {

    private final DecimalFormat decimalFormat;

    private Economy economy;

    public ClipAutosellProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        this.decimalFormat = new DecimalFormat("###,###.00");

        RegisteredServiceProvider<Economy> rsp = instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            instance.getLogger().warning("Failed to hook into AutoSell. Vault provider was unsuccessfully hooked. Money Component disabled. This may cause issues.");
            return;
        }
        this.economy = rsp.getProvider();
        instance.getAPI().registerProviders(this, SellAllEvent.class, AutoSellEvent.class);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostT(SellAllEvent event) {
        event.setTotalCost(resolveNewAmount(event.getPlayer(), event.getTotalCost()));
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostV(AutoSellEvent event) {
        Player player = event.getPlayer();
        double currentSellPrice = event.getPrice();
        double multi = instance.getHandler().getBoostService().calculatePercentage(type, player);
        double amountToGive = ((currentSellPrice * multi) - currentSellPrice);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, multi, amountToGive, type);
        if (!isAsync())
            instance.getServer().getPluginManager().callEvent(boostEvent);
        double newSellPrice = currentSellPrice + amountToGive + boostEvent.getNewEarnings();
        // run debug
        instance.getHandler().getDebugManager().runDebug(player, currentSellPrice, newSellPrice);
        // run messages
        instance.getHandler().getMessageManager().runMessages(player, type, amountToGive + boostEvent.getNewEarnings(), decimalFormat);
        economy.depositPlayer(player, amountToGive + boostEvent.getNewEarnings());
    }
}
