package me.ilucah.advancedarmor.handler.apimanager.event;

import me.ilucah.advancedarmor.armor.BoostType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmorBoostGiveEvent extends Event {

    private static HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final double amountGiven;
    private final BoostType boostType;

    public ArmorBoostGiveEvent(Player player, double amountGiven, BoostType boostType) {
        this.player = player;
        this.amountGiven = amountGiven;
        this.boostType = boostType;
    }

    public Player getPlayer() {
        return player;
    }

    public BoostType getBoostType() {
        return boostType;
    }

    public double getAmountGiven() {
        return amountGiven;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
