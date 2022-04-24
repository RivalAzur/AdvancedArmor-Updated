package me.ilucah.advancedarmor.armor.listeners;

import dev.drawethree.ultraprisoncore.UltraPrisonCore;
import dev.drawethree.ultraprisoncore.api.enums.ReceiveCause;
import dev.drawethree.ultraprisoncore.gems.api.events.PlayerGemsReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.apimanager.GemPlayer;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.boost.GemUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class UPCGemListener implements Listener {

    private final AdvancedArmor plugin;
    private final GemUtils gemUtils;
    private final DecimalFormat decimalFormat;

    public UPCGemListener(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.gemUtils = new GemUtils(plugin.getHandler());
        this.decimalFormat = new DecimalFormat("###,###.00");
    }

    @EventHandler
    public void onGemReceive(PlayerGemsReceiveEvent event) {
        if (event.getCause() == ReceiveCause.MINING ||
                event.getCause() == ReceiveCause.LUCKY_BLOCK ||
                event.getCause() == ReceiveCause.MINING_OTHERS) {
            final Player player = event.getPlayer().getPlayer();
            final GemPlayer gemPlayer = new GemPlayer(plugin.getHandler(), player);
            if (!gemPlayer.hasCustomArmorEquipped())
                return;
            long amount = event.getAmount();
            double gemMulti = gemUtils.calculatePercentage(player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                    player.getInventory().getBoots());
            long amountToGive = (long) ((amount * gemMulti) - amount);
            ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, gemMulti, amount, BoostType.GEM);
            plugin.getServer().getPluginManager().callEvent(boostEvent);

            UltraPrisonCore.getInstance().getGems().getApi().addGems(event.getPlayer(), amountToGive + (long) boostEvent.getNewEarnings(), ReceiveCause.GIVE);

            if (plugin.getHandler().getMessageManager().isGemIsEnabled()) {
                if (amountToGive != 0) {
                    plugin.getHandler().getMessageManager().getGemMessage().iterator().forEachRemaining(s -> {
                        if (s.contains("%amount%")) {
                            int string = (int) (amountToGive) + (int) boostEvent.getNewEarnings();
                            s = s.replace("%amount%", Integer.toString(string));
                        }
                        player.sendMessage(RGBParser.parse(s));
                    });
                }
            }

            if (plugin.getHandler().getDebugManager().isEnabled()) {
                player.sendMessage("Amount" + amount);
                player.sendMessage("Multi" + gemMulti);
                player.sendMessage("AmountToGive" + amountToGive);
            }
        }
    }
}
