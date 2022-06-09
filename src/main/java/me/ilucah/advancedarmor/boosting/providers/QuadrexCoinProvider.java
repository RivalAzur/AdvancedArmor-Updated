package me.ilucah.advancedarmor.boosting.providers;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.BoostProvider;
import me.ilucah.advancedarmor.api.events.ArmorBoostGiveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.quadrex.mobcoins.events.MobCoinEarnEvent;
import org.quadrex.mobcoins.storage.Database;

public class QuadrexCoinProvider extends BoostProvider<MobCoinEarnEvent> {

    public QuadrexCoinProvider(AdvancedArmor instance) {
        super(instance, BoostType.COIN);
        instance.getAPI().registerBoostProvider(MobCoinEarnEvent.class, this);
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(MobCoinEarnEvent event) {
        double currentSellPrice = event.getAmount();
        Player player = event.getPlayer();
        double multi = instance.getHandler().getBoostService().calculatePercentage(type, player);
        double amountToGive = ((currentSellPrice * multi) - currentSellPrice);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, multi, amountToGive, type);
        if (!isAsync())
            instance.getServer().getPluginManager().callEvent(boostEvent);
        double newSellPrice = currentSellPrice + amountToGive + boostEvent.getNewEarnings();
        // run debug
        instance.getHandler().getDebugManager().runDebug(player, currentSellPrice, newSellPrice);
        // run messages
        instance.getHandler().getMessageManager().runMessages(player, type, amountToGive + boostEvent.getNewEarnings());
        addCoins(player, (long) (amountToGive + boostEvent.getNewEarnings()));
    }

    private void addCoins(Player p, long amount) {
        long balance = Database.getInstance().getBalance(p.getUniqueId());
        balance += amount;
        Database.getInstance().setBalance(p.getUniqueId(), balance);
    }
}
