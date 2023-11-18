package me.ilucah.advancedarmor.listener;

import com.iridium.iridiumcolorapi.*;
import de.tr7zw.nbtapi.*;
import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.utilities.*;
import me.rivaldev.harvesterhoes.api.events.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

import java.util.concurrent.*;

public class RivalHoesListener implements Listener {



    @EventHandler
    public void onBreak(RivalBlockBreakEvent event){
        if (event.isCancelled()) return;
        if (!ShardListener.blockShardMap.containsKey(event.getCrop().getType()))
            return;
        Player player = event.getPlayer();

        ShardListener.getShardFromMaterial(event.getCrop().getType()).forEach((shard, chance) -> {
            double random = ThreadLocalRandom.current().nextDouble(0, 100); // Generate a random number between 0 and 100
        if (random <= chance) {
                NBTItem nbtItem = new NBTItem(ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Shards." + shard.getShardTypeName() + ".shard-item")).build(), true);
                nbtItem.setBoolean("ArmorShard", Boolean.TRUE);
                player.getInventory().addItem(nbtItem.getItem());
            if (AdvancedArmor.instance.getConfigManager().getShards().contains("Shards." + shard.getShardTypeName() + ".message")){
                player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Shards." + shard.getShardTypeName() + ".message")));
            }
            }
        });
    }


}
