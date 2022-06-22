package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import me.revils.revenchants.events.CurrencyReceiveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class RevEnchantsGemProvider extends BoostProvider<CurrencyReceiveEvent> {

    public RevEnchantsGemProvider(AdvancedArmor instance) {
        super(instance, BoostType.GEM);
        instance.getAPI().registerBoostProvider(CurrencyReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(CurrencyReceiveEvent event) {
        if (event.getCurrencyID().equalsIgnoreCase("gem") || event.getCurrencyID().equalsIgnoreCase("gems"))
            event.setAmount((long) resolveNewAmount(event.getPlayer(), event.getAmount()));
    }
}
