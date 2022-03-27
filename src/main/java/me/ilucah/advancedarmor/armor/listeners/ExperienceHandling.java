package me.ilucah.advancedarmor.armor.listeners;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.handler.apimanager.event.ArmorBoostGiveEvent;
import me.ilucah.advancedarmor.utilities.DebugManager;
import me.ilucah.advancedarmor.utilities.ExpUtils;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.text.DecimalFormat;

public class ExperienceHandling implements Listener {

    private final Handler handler;
    private final AdvancedArmor main;
    private final ExpUtils expUtils;
    private final DecimalFormat decimalFormat;

    public ExperienceHandling(Handler handler, AdvancedArmor main) {
        this.handler = handler;
        this.main = main;
        this.expUtils = new ExpUtils(handler);
        this.decimalFormat = new DecimalFormat("###,###.00");
    }

    @EventHandler
    public void onExperienceReceive(PlayerExpChangeEvent event) {
        final Player player = event.getPlayer();

        double expMulti = expUtils.calculatePercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
        player.giveExp((int) ((event.getAmount() * expMulti) - event.getAmount()));
        ArmorBoostGiveEvent boostEvent = new ArmorBoostGiveEvent(player, (event.getAmount() * expMulti) - event.getAmount(), BoostType.EXP);
        Bukkit.getPluginManager().callEvent(boostEvent);

        if (main.getHandler().getMessageManager().isExpIsEnabled()) {
            if (((int) ((event.getAmount() * expMulti) - event.getAmount())) != 0) {
                main.getHandler().getMessageManager().getExpMessage().iterator().forEachRemaining(s -> {
                    if (s.contains("%amount%"))
                        s = s.replace("%amount%", String.valueOf(decimalFormat.format((int) ((event.getAmount() * expMulti) - event.getAmount()))));
                    player.sendMessage(RGBParser.parse(s));
                });
            }
        }

        if (main.getHandler().getDebugManager().isEnabled()) {
            main.getHandler().getDebugManager().expEventDebugInfoSend(event.getAmount(),
                    (int) ((event.getAmount() * expMulti) - event.getAmount()),
                    (int) ((event.getAmount() * expMulti)), expMulti);
            main.getHandler().getDebugManager().expEventDebugInfoSend(player, event.getAmount(),
                    (int) ((event.getAmount() * expMulti) - event.getAmount()),
                    (int) ((event.getAmount() * expMulti)), expMulti);
        }
    }

}
