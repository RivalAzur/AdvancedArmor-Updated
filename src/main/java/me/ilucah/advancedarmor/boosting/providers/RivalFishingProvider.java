package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import me.rivaldev.fishingrod.rivalfishingrods.api.RodEssenceReceiveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class RivalFishingProvider extends BoostProvider<RodEssenceReceiveEvent> {

    public RivalFishingProvider(AdvancedArmor instance) {
        super(instance, BoostType.ESSENCE);
        instance.getAPI().registerBoostProvider(RodEssenceReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(RodEssenceReceiveEvent event) {
        event.setEssence(resolveNewAmount(event.getPlayer(), event.getEssence()));
    }
}
