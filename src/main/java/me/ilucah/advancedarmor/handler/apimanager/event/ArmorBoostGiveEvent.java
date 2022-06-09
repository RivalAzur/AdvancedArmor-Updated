package me.ilucah.advancedarmor.handler.apimanager.event;

import me.ilucah.advancedarmor.armor.BoostType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Explicit NOT-ASYNC
 */
public class ArmorBoostGiveEvent extends Event {

    public static void callSync(Plugin plugin, Player player, double boost, double amountGiven, BoostType boostType) {
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new ArmorBoostGiveEvent(player, boost, amountGiven, boostType)));
    }

    public static void callSync(Plugin plugin, ArmorBoostGiveEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(event));
    }

    private static HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private double amountGiven, boost, newEarnings;
    private final BoostType boostType;

    public ArmorBoostGiveEvent(Player player, double boost, double amountGiven, BoostType boostType) {
        super(false);
        this.player = player;
        this.amountGiven = amountGiven;
        this.boostType = boostType;
        this.boost = boost;
        this.newEarnings = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public BoostType getBoostType() {
        return boostType;
    }

    public double getOriginalAmount() {
        return amountGiven;
    }

    public void addNewEarnings(double amount) {
        newEarnings += amount;
    }

    public double getNewEarnings() {
        return newEarnings;
    }

    public double getBoost() {
        return boost;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
