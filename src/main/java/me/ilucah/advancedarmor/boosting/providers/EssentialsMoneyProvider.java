package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.BoostProvider;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.math.BigDecimal;

public class EssentialsMoneyProvider extends BoostProvider<UserBalanceUpdateEvent> {

    public EssentialsMoneyProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(UserBalanceUpdateEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(UserBalanceUpdateEvent event) {
        System.out.println("a");
        if (event.getCause() == UserBalanceUpdateEvent.Cause.COMMAND_PAY && event.getCause() == UserBalanceUpdateEvent.Cause.COMMAND_ECO)
            return;
        System.out.println("b");
        if (event.getNewBalance().compareTo(event.getOldBalance()) < 0)
            return;
        System.out.println("c");
        event.setNewBalance(BigDecimal.valueOf(resolveNewAmount(event.getPlayer(), event.getNewBalance().doubleValue() - event.getOldBalance().doubleValue())));
    }
}
