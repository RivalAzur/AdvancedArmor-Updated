package me.ilucah.advancedarmor.handler.apimanager.event;

import me.ilucah.advancedarmor.armor.Armor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ArmorGiveItemEvent extends Event {

    private static HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Armor armor;
    private ItemStack item;

    public ArmorGiveItemEvent(Player player, Armor armor, ItemStack item) {
        this.player = player;
        this.armor = armor;
        this.item = item;
    }

    public Player getPlayer() {
        return player;
    }

    public Armor getArmor() {
        return armor;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
