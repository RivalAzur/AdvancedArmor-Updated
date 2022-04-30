package me.ilucah.advancedarmor.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SkullPlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() == null)
            return;
        if (event.getItemInHand().getType() == Material.AIR)
            return;
        NBTItem item = new NBTItem(event.getItemInHand());
        if (item.hasKey("CustomArmor"))
            event.setCancelled(true);
    }
}
