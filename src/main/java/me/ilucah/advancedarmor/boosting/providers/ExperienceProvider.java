package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.BoostProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExperienceProvider extends BoostProvider<PlayerExpChangeEvent> {

    public ExperienceProvider(AdvancedArmor instance) {
        super(instance, BoostType.EXP);
        instance.getAPI().registerBoostProvider(PlayerExpChangeEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(PlayerExpChangeEvent event) {
        event.setAmount((int) resolveNewAmount(event.getPlayer(), event.getAmount()));
    }
}
