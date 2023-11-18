package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.armor.*;
import me.ilucah.advancedarmor.boosting.model.*;
import net.devtm.tmmobcoins.API.*;
import org.bukkit.event.*;
import org.hassan.plugin.collector.event.*;

public class ManifestCollectorProvider extends BoostProvider<CollectorSellEvent> {

    public ManifestCollectorProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(CollectorSellEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(CollectorSellEvent event) {
        event.setPrice((float) resolveNewAmount(event.getPlayer(), event.getPrice()));
    }
}
