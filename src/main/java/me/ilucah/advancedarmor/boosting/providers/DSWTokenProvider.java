package me.ilucah.advancedarmor.boosting.providers;

import dev.norska.dsw.api.DeluxeSellwandTokenReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class DSWTokenProvider extends BoostProvider<DeluxeSellwandTokenReceiveEvent> {

    public DSWTokenProvider(AdvancedArmor instance) {
        super(instance, BoostType.TOKEN);
        instance.getAPI().registerBoostProvider(DeluxeSellwandTokenReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(DeluxeSellwandTokenReceiveEvent event) {
        event.setTokens((int) resolveNewAmount(event.getPlayer(), event.getTokens()));
    }
}
