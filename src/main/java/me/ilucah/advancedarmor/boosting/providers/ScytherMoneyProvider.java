package me.ilucah.advancedarmor.boosting.providers;

import dev.norska.scyther.api.ScytherAutosellEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BiBoostProvider;
import me.ilucah.advancedarmor.api.events.ArmorBoostGiveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ScytherMoneyProvider extends BiBoostProvider<ScytherAutosellEvent, ScytherAutosellEvent> {

    public ScytherMoneyProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerProviders(this, ScytherAutosellEvent.class);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostT(ScytherAutosellEvent event) {
        event.setMoney(resolveNewAmount(event.getPlayer(), event.getMoney()));
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostV(ScytherAutosellEvent event) {
        double currentSellPrice = event.getAwardedXP();
        double multi = instance.getHandler().getBoostService().calculatePercentage(BoostType.EXP, event.getPlayer());
        double amountToGive = ((currentSellPrice * multi) - currentSellPrice);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(event.getPlayer(), multi, amountToGive, BoostType.EXP);
        if (!isAsync())
            instance.getServer().getPluginManager().callEvent(boostEvent);
        double newSellPrice = currentSellPrice + amountToGive + boostEvent.getNewEarnings();
        // run debug
        instance.getHandler().getDebugManager().runDebug(event.getPlayer(), currentSellPrice, newSellPrice);
        // run messages
        instance.getHandler().getMessageManager().runMessages(event.getPlayer(), BoostType.EXP, amountToGive + boostEvent.getNewEarnings());
        event.setAwardedXP(amountToGive + boostEvent.getNewEarnings());
    }
}
