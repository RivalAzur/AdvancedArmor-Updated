package me.ilucah.advancedarmor.boosting.providers;

import dev.drawethree.ultraprisoncore.UltraPrisonCore;
import dev.drawethree.ultraprisoncore.autosell.api.events.UltraPrisonAutoSellEvent;
import dev.drawethree.ultraprisoncore.autosell.api.events.UltraPrisonSellAllEvent;
import dev.drawethree.ultraprisoncore.multipliers.enums.MultiplierType;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BiBoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class UPCMoneyProvider extends BiBoostProvider<UltraPrisonAutoSellEvent, UltraPrisonSellAllEvent> {

    public UPCMoneyProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerProviders(
                this,
                UltraPrisonAutoSellEvent.class,
                UltraPrisonSellAllEvent.class);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostT(UltraPrisonAutoSellEvent event) {
        double totalAmount = event.getItemsToSell().values().stream().mapToDouble(Double::doubleValue).sum();
        if (UltraPrisonCore.getInstance().getAutoSell().isMultipliersModuleEnabled())
            totalAmount = (long) UltraPrisonCore.getInstance().getMultipliers().getApi().getTotalToDeposit(event.getPlayer(), totalAmount, MultiplierType.SELL);
        double amountToGive = resolveNewAmount(event.getPlayer(), totalAmount) - totalAmount;
        UltraPrisonCore.getInstance().getEconomy().depositPlayer(event.getPlayer(), amountToGive);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostV(UltraPrisonSellAllEvent event) {
        double totalAmount = event.getItemsToSell().values().stream().mapToDouble(Double::doubleValue).sum();
        if (UltraPrisonCore.getInstance().getAutoSell().isMultipliersModuleEnabled())
            totalAmount = (long) UltraPrisonCore.getInstance().getMultipliers().getApi().getTotalToDeposit(event.getPlayer(), totalAmount, MultiplierType.SELL);
        double amountToGive = resolveNewAmount(event.getPlayer(), totalAmount) - totalAmount;
        UltraPrisonCore.getInstance().getEconomy().depositPlayer(event.getPlayer(), amountToGive);
    }
}
