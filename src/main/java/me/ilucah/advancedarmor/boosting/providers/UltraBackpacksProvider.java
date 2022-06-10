package me.ilucah.advancedarmor.boosting.providers;

import dev.drawethree.ultrabackpacks.api.event.BackpackSellEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class UltraBackpacksProvider extends BoostProvider<BackpackSellEvent> {

    public UltraBackpacksProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(BackpackSellEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(BackpackSellEvent event) {
        event.setMoneyToDeposit(resolveNewAmount(event.getPlayer(), event.getMoneyToDeposit()));
    }
}
