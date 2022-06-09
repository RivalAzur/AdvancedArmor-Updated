package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.math.BigDecimal;

public class EssentialsMoneyProvider extends BoostProvider<UserBalanceUpdateEvent> {

    public EssentialsMoneyProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY, true);
        instance.getAPI().registerBoostProvider(UserBalanceUpdateEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(UserBalanceUpdateEvent event) {
        if (event.getCause() == UserBalanceUpdateEvent.Cause.COMMAND_PAY && event.getCause() == UserBalanceUpdateEvent.Cause.COMMAND_ECO)
            return;
        if (event.getNewBalance().compareTo(event.getOldBalance()) < 0)
            return;
        event.setNewBalance(BigDecimal.valueOf(resolveNewAmount(event.getPlayer(), event.getNewBalance().doubleValue() - event.getOldBalance().doubleValue())));
    }
}
