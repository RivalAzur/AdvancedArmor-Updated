package me.ilucah.advancedarmor.boosting.providers;

import dev.paracausal.warriormobcoins.api.events.*;
import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.armor.*;
import me.ilucah.advancedarmor.boosting.model.*;
import org.bukkit.event.*;

public class WarriorMobCoinsProvider extends BoostProvider<MobCoinDropEvent> {

    public WarriorMobCoinsProvider(AdvancedArmor instance) {
        super(instance, BoostType.COIN);
        instance.getAPI().registerBoostProvider(MobCoinDropEvent .class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(MobCoinDropEvent  event) {
        double amount = event.getAmount().doubleValue();
        event.setAmount( resolveNewAmount(event.getPlayer(), amount));
    }
}
