package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import me.ilucah.advancedarmor.api.events.ArmorBoostGiveEvent;
import net.luckyfeed.InfiniteChests;
import net.luckyfeed.events.InfiniteChestSellEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class InfiniteChestProProvider extends BoostProvider<InfiniteChestSellEvent> {

    public InfiniteChestProProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(InfiniteChestSellEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(InfiniteChestSellEvent event) {
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
        InfiniteChests.getInstance().getEconomy().depositPlayer(event.getRecipient(), amountToGive + boostEvent.getNewEarnings());
    }
}
