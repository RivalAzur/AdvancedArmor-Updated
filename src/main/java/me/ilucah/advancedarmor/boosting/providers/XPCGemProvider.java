package me.ilucah.advancedarmor.boosting.providers;

import dev.drawethree.xprison.api.enums.*;
import dev.drawethree.xprison.gems.api.events.*;
import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.armor.*;
import me.ilucah.advancedarmor.boosting.model.*;
import org.bukkit.event.*;

public class XPCGemProvider extends BoostProvider<PlayerGemsReceiveEvent> {

    public XPCGemProvider(AdvancedArmor instance) {
        super(instance, BoostType.GEM);
        instance.getAPI().registerBoostProvider(PlayerGemsReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(PlayerGemsReceiveEvent event) {
        if (!event.getPlayer().isOnline())
            return;
        if (event.getCause() != ReceiveCause.MINING && event.getCause() != ReceiveCause.LUCKY_BLOCK &&
                event.getCause() != ReceiveCause.MINING_OTHERS)
            return;
        event.setAmount((long) resolveNewAmount(event.getPlayer().getPlayer(), event.getAmount()));
    }
}