package me.ilucah.advancedarmor.boosting.providers;

import dev.norska.dsw.api.DeluxeSellwandSellEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class DSWMoneyProvider extends BoostProvider<DeluxeSellwandSellEvent> {

    public DSWMoneyProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(DeluxeSellwandSellEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(DeluxeSellwandSellEvent event) {
        event.setMoney(resolveNewAmount(event.getPlayer(), event.getMoney()));
    }
}
