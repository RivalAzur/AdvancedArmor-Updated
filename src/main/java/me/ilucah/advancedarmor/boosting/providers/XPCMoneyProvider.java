package me.ilucah.advancedarmor.boosting.providers;

import dev.drawethree.xprison.*;
import dev.drawethree.xprison.autosell.api.events.*;
import dev.drawethree.xprison.multipliers.enums.*;
import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.armor.*;
import me.ilucah.advancedarmor.boosting.model.*;
import org.bukkit.event.*;

public class XPCMoneyProvider  extends BiBoostProvider<XPrisonAutoSellEvent, XPrisonSellAllEvent> {

    public XPCMoneyProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerProviders(
                this,
                XPrisonAutoSellEvent.class,
                XPrisonSellAllEvent.class);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostT(XPrisonAutoSellEvent event) {
        double totalAmount = event.getItemsToSell().values().stream().mapToDouble(Double::doubleValue).sum();
        if (XPrison.getInstance().getAutoSell().isMultipliersModuleEnabled())
            totalAmount = (long) XPrison.getInstance().getMultipliers().getApi().getTotalToDeposit(event.getPlayer(), totalAmount, MultiplierType.SELL);
        double amountToGive = resolveNewAmount(event.getPlayer(), totalAmount) - totalAmount;
        XPrison.getInstance().getEconomy().depositPlayer(event.getPlayer(), amountToGive);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostV(XPrisonSellAllEvent event) {
        double totalAmount = event.getItemsToSell().values().stream().mapToDouble(Double::doubleValue).sum();
        if (XPrison.getInstance().getAutoSell().isMultipliersModuleEnabled())
            totalAmount = (long) XPrison.getInstance().getMultipliers().getApi().getTotalToDeposit(event.getPlayer(), totalAmount, MultiplierType.SELL);
        double amountToGive = resolveNewAmount(event.getPlayer(), totalAmount) - totalAmount;
        XPrison.getInstance().getEconomy().depositPlayer(event.getPlayer(), amountToGive);
    }
}