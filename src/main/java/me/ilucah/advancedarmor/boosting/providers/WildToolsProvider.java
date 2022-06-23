package me.ilucah.advancedarmor.boosting.providers;

import com.bgsoftware.wildtools.api.events.HarvesterHoeSellEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class WildToolsProvider extends BoostProvider<HarvesterHoeSellEvent> {

    public WildToolsProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(HarvesterHoeSellEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(HarvesterHoeSellEvent event) {
        event.setPrice(resolveNewAmount(event.getPlayer(), event.getPrice()));
    }
}
