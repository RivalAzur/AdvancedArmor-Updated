package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import me.rivaldev.harvesterhoes.api.events.HoeEssenceReceivePreEnchantEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class RivalHoesProvider extends BoostProvider<HoeEssenceReceivePreEnchantEvent> {

    public RivalHoesProvider(AdvancedArmor instance) {
        super(instance, BoostType.ESSENCE);
        instance.getAPI().registerBoostProvider(HoeEssenceReceivePreEnchantEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(HoeEssenceReceivePreEnchantEvent event) {
        event.setEssence(resolveNewAmount(event.getPlayer(), event.getEssence()));
    }
}
