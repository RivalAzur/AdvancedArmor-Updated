package me.ilucah.advancedarmor.boosting;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.api.events.ArmorBoostGiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public abstract class BoostProvider<T extends Event> implements Listener, TypeProvider<T> {

    protected final BoostType type;
    protected final AdvancedArmor instance;
    private boolean async;

    public BoostProvider(AdvancedArmor instance, BoostType type) {
        this.instance = instance;
        this.type = type;
    }

    public BoostProvider(AdvancedArmor instance, BoostType type, boolean async) {
        this(instance, type);
        this.async = async;
    }

    public double resolveNewAmount(Player player, double currentSellPrice) {
        double multi = instance.getHandler().getBoostService().calculatePercentage(type, player);
        double amountToGive = ((currentSellPrice * multi) - currentSellPrice);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, multi, amountToGive, type);
        if (!async)
            instance.getServer().getPluginManager().callEvent(boostEvent);
        double newSellPrice = currentSellPrice + amountToGive + boostEvent.getNewEarnings();
        instance.getHandler().getDebugManager().runDebug(player, currentSellPrice, newSellPrice);
        instance.getHandler().getMessageManager().runMessages(player, type, amountToGive + boostEvent.getNewEarnings());
        return currentSellPrice + amountToGive + boostEvent.getNewEarnings();
    }

    /**
     * [0] = new amount to set
     * [1] = message amount
     */
    public double[] resolveRawSell(Player player, double currentSellPrice) {
        double multi = instance.getHandler().getBoostService().calculatePercentage(type, player);
        double amountToGive = ((currentSellPrice * multi) - currentSellPrice);
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, multi, amountToGive, type);
        if (!async)
            instance.getServer().getPluginManager().callEvent(boostEvent);
        else
            ArmorBoostGiveEvent.callSync(instance, boostEvent);
        return new double[]{
                currentSellPrice + amountToGive + boostEvent.getNewEarnings(),
                amountToGive + boostEvent.getNewEarnings()
        };
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, instance);
        instance.getLogger().info("Registered a " + type.name() + " boost listener.");
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * @implNote <code>@EventHandler(priority = EventPriority.LOWEST)</code>
     * @apiNote
     * This method must include the EventHandler annotation.
     */
    public abstract void onSell(T event);

}
