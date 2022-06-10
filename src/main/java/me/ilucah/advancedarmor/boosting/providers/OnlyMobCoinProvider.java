package me.ilucah.advancedarmor.boosting.providers;

import me.aglerr.mobcoins.api.events.MobCoinsReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class OnlyMobCoinProvider extends BoostProvider<MobCoinsReceiveEvent> {

    public OnlyMobCoinProvider(AdvancedArmor instance) {
        super(instance, BoostType.COIN);
        instance.getAPI().registerBoostProvider(MobCoinsReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(MobCoinsReceiveEvent event) {
        event.setAmountReceived(resolveNewAmount(event.getPlayer(), event.getAmountReceived()));
    }
}
