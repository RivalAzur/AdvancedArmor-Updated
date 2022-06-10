package me.ilucah.advancedarmor.boosting.providers;

import me.aglerr.krakenmobcoins.api.events.MobCoinsReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class KrakenMobCoinProvider extends BoostProvider<MobCoinsReceiveEvent> {

    public KrakenMobCoinProvider(AdvancedArmor instance) {
        super(instance, BoostType.COIN);
        instance.getAPI().registerBoostProvider(MobCoinsReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(MobCoinsReceiveEvent event) {
        event.setAmountAfterMultiplier(resolveNewAmount(event.getPlayer(), event.getAmountAfterMultiplier()));
    }
}
