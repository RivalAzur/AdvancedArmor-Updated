package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.BoostProvider;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.ichest.IChestHookManager;
import net.luckyfeed.events.InfiniteChestSellEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class InfiniteChestProProvider extends BoostProvider<InfiniteChestSellEvent> {

    private final IChestHookManager manager;

    public InfiniteChestProProvider(AdvancedArmor instance, IChestHookManager manager) {
        super(instance, BoostType.MONEY);
        this.manager = manager;
        instance.getAPI().registerBoostProvider(InfiniteChestSellEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(InfiniteChestSellEvent event) {
        if (!event.getRecipient().isOnline())
            return;
        Player player = event.getRecipient().getPlayer();
        double currentSellPrice = event.getFinalPrice();
        double multi = instance.getHandler().getBoostService().calculatePercentage(type, player);
        double amountToGive = ((currentSellPrice * multi) - currentSellPrice);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, multi, amountToGive, type);
        if (!isAsync())
            instance.getServer().getPluginManager().callEvent(boostEvent);
        double newSellPrice = currentSellPrice + amountToGive + boostEvent.getNewEarnings();
        // run debug
        instance.getHandler().getDebugManager().runDebug(player, currentSellPrice, newSellPrice);
        // run messages
        instance.getHandler().getMessageManager().runMessages(player, type, amountToGive + boostEvent.getNewEarnings());
        manager.getEconomyHook().deposit(event.getRecipient(),amountToGive + boostEvent.getNewEarnings());
    }
}
