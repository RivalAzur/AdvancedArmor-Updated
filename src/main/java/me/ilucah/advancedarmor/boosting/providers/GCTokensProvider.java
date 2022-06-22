package me.ilucah.advancedarmor.boosting.providers;

import com.solodevelopment.tokens.enchant.events.SystemTokenReceiveEvent;
import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.boosting.model.BoostProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class GCTokensProvider extends BoostProvider<SystemTokenReceiveEvent> {

    public GCTokensProvider(AdvancedArmor instance) {
        super(instance, BoostType.TOKEN);
        instance.getAPI().registerBoostProvider(SystemTokenReceiveEvent.class, this);
    }

    @Override
    public void onBoost(SystemTokenReceiveEvent event) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPlayerUUID());
        if (!player.isOnline())
            return;
        event.getPlayerStorage().addTokens((long) (resolveNewAmount(player.getPlayer(), event.getTokensAmount()) - event.getTokensAmount()));
    }
}
