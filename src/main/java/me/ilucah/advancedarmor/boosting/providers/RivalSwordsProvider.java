package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.BoostProvider;
import me.rivaldev.mobsword.rivalmobswords.api.SwordEssenceReceivePreEnchantEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class RivalSwordsProvider extends BoostProvider<SwordEssenceReceivePreEnchantEvent> {

    public RivalSwordsProvider(AdvancedArmor instance) {
        super(instance, BoostType.ESSENCE);
        instance.getAPI().registerBoostProvider(SwordEssenceReceivePreEnchantEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(SwordEssenceReceivePreEnchantEvent event) {
        event.setEssence(resolveNewAmount(event.getPlayer(), event.getEssence()));
    }
}
