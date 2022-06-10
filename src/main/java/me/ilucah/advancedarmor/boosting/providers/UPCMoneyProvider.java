package me.ilucah.advancedarmor.boosting.providers;

import dev.drawethree.ultraprisoncore.autosell.api.events.UltraPrisonAutoSellEvent;
import dev.drawethree.ultraprisoncore.autosell.api.events.UltraPrisonSellAllEvent;
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
        event.setMoneyToDeposit(resolveNewAmount(event.getPlayer(), event.getMoneyToDeposit()));
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoostV(UltraPrisonSellAllEvent event) {
        event.setSellPrice(resolveNewAmount(event.getPlayer(), event.getSellPrice()));
    }
}
