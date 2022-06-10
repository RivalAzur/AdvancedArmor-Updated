package me.ilucah.advancedarmor.boosting.providers;

import dev.drawethree.ultraprisoncore.api.enums.ReceiveCause;
import dev.drawethree.ultraprisoncore.tokens.api.events.PlayerTokensReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class UPCTokenProvider extends BoostProvider<PlayerTokensReceiveEvent> {

    public UPCTokenProvider(AdvancedArmor instance) {
        super(instance, BoostType.TOKEN);
        instance.getAPI().registerBoostProvider(PlayerTokensReceiveEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(PlayerTokensReceiveEvent event) {
        if (!event.getPlayer().isOnline())
            return;
        if (event.getCause() != ReceiveCause.MINING || event.getCause() != ReceiveCause.LUCKY_BLOCK ||
                event.getCause() != ReceiveCause.MINING_OTHERS)
            return;
        event.setAmount((long) resolveNewAmount(event.getPlayer().getPlayer(), event.getAmount()));
    }
}
