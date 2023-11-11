package me.ilucah.advancedarmor.boosting.providers;

import me.gypopo.economyshopgui.api.events.PreTransactionEvent;
import me.gypopo.economyshopgui.util.*;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class EconomyShopProvider extends BoostProvider<PreTransactionEvent> {

    public EconomyShopProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(PreTransactionEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(PreTransactionEvent event) {
        if (event.getTransactionType() == Transaction.Type.QUICK_SELL ||event.getTransactionType() == Transaction.Type.SELL_ALL_COMMAND || event.getTransactionType() == Transaction.Type.SELL_ALL_SCREEN || event.getTransactionType() == Transaction.Type.SELL_GUI_SCREEN || event.getTransactionType() == Transaction.Type.SELL_SCREEN|| event.getTransactionType() == Transaction.Type.AUTO_SELL_CHEST     )
            event.setPrice(resolveNewAmount(event.getPlayer(), event.getPrice()));
    }
}
