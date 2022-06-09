package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import net.devtm.tmmobcoins.API.MobCoinReceiveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class TMCoinProvider extends BoostProvider<MobCoinReceiveEvent> {

    public TMCoinProvider(AdvancedArmor instance) {
        super(instance, BoostType.COIN);
        instance.getAPI().registerBoostProvider(MobCoinReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(MobCoinReceiveEvent event) {
        event.setDropAmount(resolveNewAmount(event.getPlayer(), event.getObtainedAmount()));
    }
}
