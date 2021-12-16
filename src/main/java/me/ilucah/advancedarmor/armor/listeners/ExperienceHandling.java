package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.ExpUtils;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExperienceHandling implements Listener {

    private final Handler handler;
    private final AdvancedArmor main;

    public ExperienceHandling(Handler handler, AdvancedArmor main) {
        this.handler = handler;
        this.main = main;
    }

    @EventHandler
    public void onExperienceReceive(PlayerExpChangeEvent event) {
        final Player player = event.getPlayer();
        final ExpUtils expUtils = new ExpUtils(handler);
        final DebugManager debugManager = new DebugManager(main);
        final MessageUtils messageUtils = new MessageUtils(main);
        double expMulti = expUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        player.giveExp((int) ((event.getAmount() * expMulti) - event.getAmount()));
        if (((event.getAmount() * expMulti) - event.getAmount()) > 0.0) {
            messageUtils.getConfigMessage("BoostMessages.EXP.Message").iterator().forEachRemaining(s -> {
                if (s.contains("%amount%"))
                    s = s.replace("%amount%", String.valueOf((int) ((event.getAmount() * expMulti) - event.getAmount())));
                player.sendMessage(s);
            });
        }

        if (debugManager.isEnabled()) {
            debugManager.expEventDebugInfoSend(event.getAmount(),
                    (int) ((event.getAmount() * expMulti) - event.getAmount()),
                    (int) ((event.getAmount() * expMulti)), expMulti);
            debugManager.expEventDebugInfoSend(player, event.getAmount(),
                    (int) ((event.getAmount() * expMulti) - event.getAmount()),
                    (int) ((event.getAmount() * expMulti)), expMulti);
        }
    }

}
