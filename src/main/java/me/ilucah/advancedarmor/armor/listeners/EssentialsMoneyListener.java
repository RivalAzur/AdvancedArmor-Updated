package me.ilucah.advancedarmor.armor.listeners;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import net.ess3.api.MaxMoneyException;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;

public class EssentialsMoneyListener implements Listener {

    private AdvancedArmor plugin;
    private Handler handler;

    public EssentialsMoneyListener(Handler handler, AdvancedArmor plugin) {
        this.plugin = plugin;
        this.handler = handler;
    }

    @EventHandler
    public void onBalanceChangeEvent(UserBalanceUpdateEvent event) throws MaxMoneyException, UserDoesNotExistException, NoLoanPermittedException {
        final Player player = event.getPlayer();
        final MoneyUtils moneyUtils = new MoneyUtils(handler);
        final DebugManager debugManager = new DebugManager(plugin);
        final MessageUtils messageUtils = new MessageUtils(plugin);
        if (event.getCause() != UserBalanceUpdateEvent.Cause.COMMAND_PAY && event.getCause() != UserBalanceUpdateEvent.Cause.COMMAND_ECO) {
            if (event.getNewBalance().compareTo(event.getOldBalance()) > 0) {
                BigDecimal amountReceived = event.getNewBalance().subtract(event.getOldBalance());

                double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                        player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                        player.getInventory().getBoots());

                event.setNewBalance(event.getOldBalance().add(BigDecimal.valueOf((amountReceived.doubleValue() * moneyMulti))));
                if (plugin.getConfig().getBoolean("Messages.BoostMessages.Money.Enabled")) {
                    if (((amountReceived.doubleValue() * moneyMulti) - amountReceived.doubleValue()) != 0) {
                        messageUtils.getConfigMessage("BoostMessages.Money.Message").iterator().forEachRemaining(s -> {
                            if (s.contains("%amount%"))
                                s = s.replace("%amount%", String.valueOf((amountReceived.doubleValue() * moneyMulti) - amountReceived.doubleValue()));
                            player.sendMessage(s);
                        });
                    }
                }

                if (debugManager.isEnabled()) {
                    debugManager.moneyEventDebugInfoSend(amountReceived.doubleValue(),
                            (amountReceived.doubleValue() * moneyMulti) - amountReceived.doubleValue(),
                            (amountReceived.doubleValue() * moneyMulti), moneyMulti);
                    debugManager.moneyEventDebugInfoSend(player, amountReceived.doubleValue(),
                            (amountReceived.doubleValue() * moneyMulti) - amountReceived.doubleValue(),
                            (amountReceived.doubleValue() * moneyMulti), moneyMulti);
                }
            }
        }
    }

}
