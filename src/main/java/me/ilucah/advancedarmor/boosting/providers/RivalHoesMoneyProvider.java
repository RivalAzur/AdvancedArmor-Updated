package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import me.rivaldev.harvesterhoes.api.events.HoeMoneyReceiveEnchant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class RivalHoesMoneyProvider extends BoostProvider<HoeMoneyReceiveEnchant> {

    public RivalHoesMoneyProvider(AdvancedArmor instance) {
        super(instance, BoostType.MONEY);
        instance.getAPI().registerBoostProvider(HoeMoneyReceiveEnchant.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBoost(HoeMoneyReceiveEnchant event) {
        event.setMoney(resolveNewAmount(event.getPlayer(), event.getMoney()));
    }
}
