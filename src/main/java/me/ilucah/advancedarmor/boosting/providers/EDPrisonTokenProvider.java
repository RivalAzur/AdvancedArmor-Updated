package me.ilucah.advancedarmor.boosting.providers;

import com.edwardbelt.edprison.events.*;
import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.armor.*;
import me.ilucah.advancedarmor.boosting.model.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class EDPrisonTokenProvider extends BoostProvider<EdPrisonAddMultiplierCurrency> {

public EDPrisonTokenProvider(AdvancedArmor instance) {
        super(instance, BoostType.TOKEN);
        instance.getAPI().registerBoostProvider(EdPrisonAddMultiplierCurrency.class, this);
        }

                @Override
@EventHandler(priority = EventPriority.LOWEST)
public void onBoost(EdPrisonAddMultiplierCurrency event) {
        Player player = Bukkit.getPlayer(event.getUUID());
        if (event.getCurrency().equalsIgnoreCase("tokens")) {
                event.setAmount((long) resolveNewAmount(player, event.getAmount()));
        }
        }
        }