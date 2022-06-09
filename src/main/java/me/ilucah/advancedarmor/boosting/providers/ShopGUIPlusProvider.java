package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.BoostProvider;
import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ShopGUIPlusProvider extends BoostProvider<ShopPreTransactionEvent> {

    public ShopGUIPlusProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(ShopPreTransactionEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(ShopPreTransactionEvent event) {
        event.setPrice(resolveNewAmount(event.getPlayer(), event.getPrice()));
    }
}
