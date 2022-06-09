package me.ilucah.advancedarmor.boosting.providers;

import me.aqua.aquacoins.api.CoinReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class AquaCoinProvider extends BoostProvider<CoinReceiveEvent> {

    public AquaCoinProvider(AdvancedArmor instance) {
        super(instance, BoostType.COIN);
        instance.getAPI().registerBoostProvider(CoinReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(CoinReceiveEvent event) {
        event.setCoins((long) resolveNewAmount(event.getPlayer(), event.getCoins()));
    }
}
