package me.ilucah.advancedarmor.armor.listeners;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import net.ess3.api.MaxMoneyException;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class EssentialsMoneyListener implements Listener {

    private final AdvancedArmor plugin;
    private final Handler handler;
    private final MoneyUtils moneyUtils;
    private final DecimalFormat decimalFormat;

    public EssentialsMoneyListener(Handler handler, AdvancedArmor plugin) {
        this.plugin = plugin;
        this.handler = handler;
        this.moneyUtils = new MoneyUtils(handler);
        this.decimalFormat = new DecimalFormat( "###,###.00" );
    }

    @EventHandler
    public void onBalanceChangeEvent(UserBalanceUpdateEvent event) {
        final Player player = event.getPlayer();
        if (event.getCause() != UserBalanceUpdateEvent.Cause.COMMAND_PAY && event.getCause() != UserBalanceUpdateEvent.Cause.COMMAND_ECO) {
            if (event.getNewBalance().compareTo(event.getOldBalance()) > 0) {
                BigDecimal amountReceived = event.getNewBalance().subtract(event.getOldBalance());

                double moneyMulti = moneyUtils.calculatePercentage(player.getInventory().getHelmet(),
                        player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                        player.getInventory().getBoots());
                ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, moneyMulti, amountReceived.doubleValue(), BoostType.MONEY);
                plugin.getServer().getPluginManager().callEvent(boostEvent);

                event.setNewBalance(event.getOldBalance().add(BigDecimal.valueOf((amountReceived.doubleValue() * moneyMulti) + boostEvent.getNewEarnings())));
                if (plugin.getHandler().getMessageManager().isMoneyIsEnabled()) {
                    if (((amountReceived.doubleValue() * moneyMulti) - amountReceived.doubleValue()) != 0) {
                        plugin.getHandler().getMessageManager().getMoneyMessage().iterator().forEachRemaining(s -> {
                            if (s.contains("%amount%"))
                                s = s.replace("%amount%", String.valueOf(decimalFormat.format((amountReceived.doubleValue() * moneyMulti) - amountReceived.doubleValue() + boostEvent.getNewEarnings())));
                            player.sendMessage(RGBParser.parse(s));
                        });
                    }
                }

                if (plugin.getHandler().getDebugManager().isEnabled()) {
                    plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(amountReceived.doubleValue(),
                            (amountReceived.doubleValue() * moneyMulti) - amountReceived.doubleValue(),
                            (amountReceived.doubleValue() * moneyMulti), moneyMulti);
                    plugin.getHandler().getDebugManager().moneyEventDebugInfoSend(player, amountReceived.doubleValue(),
                            (amountReceived.doubleValue() * moneyMulti) - amountReceived.doubleValue(),
                            (amountReceived.doubleValue() * moneyMulti), moneyMulti);
                }
            }
        }
    }

}
