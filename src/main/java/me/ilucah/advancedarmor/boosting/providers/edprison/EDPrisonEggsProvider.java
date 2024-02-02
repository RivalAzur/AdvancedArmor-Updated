package me.ilucah.advancedarmor.boosting.providers.edprison;

import com.edwardbelt.edprison.events.*;
import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.armor.*;
import me.ilucah.advancedarmor.boosting.model.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class EDPrisonEggsProvider extends BoostProvider<EdPrisonAddMultiplierCurrency> {

public EDPrisonEggsProvider(AdvancedArmor instance) {
        super(instance, BoostType.EGGS);
        instance.getAPI().registerBoostProvider(EdPrisonAddMultiplierCurrency.class, this);
        }

                @Override
@EventHandler(priority = EventPriority.LOWEST)
public void onBoost(EdPrisonAddMultiplierCurrency event) {
        Player player = Bukkit.getPlayer(event.getUUID());
        if (event.getCurrency().equalsIgnoreCase("eggs")) {
                event.setAmount((long) resolveNewAmount(player, event.getAmount()));
        }
        }
        }