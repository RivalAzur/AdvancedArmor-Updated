package me.ilucah.advancedarmor.utilities.msg;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.utilities.RGBParser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MessageManager {

    private FileConfiguration messageConfig;

    private final DecimalFormat format = new DecimalFormat("#,###");
    private final ConcurrentHashMap<BoostType, List<String>> messages = new ConcurrentHashMap<>();

    public MessageManager(AdvancedArmor plugin) {
        this.messageConfig = plugin.getConfigManager().getMessages();
        loadMessages();
    }

    @Nullable
    public List<String> getMessage(BoostType boostType) {
        return messages.get(boostType);
    }

    public void runMessages(Player player, BoostType boostType, double amount) {
        runMessages(player, boostType, amount, format);
    }

    public void runMessages(Player player, BoostType boostType, double amount, DecimalFormat dFormat) {
        if (amount < 1)
            return;
        if (dFormat == null)
            return;
        List<String> message = getMessage(boostType);
        if (message == null)
            return;
        message.forEach(m -> player.sendMessage(RGBParser.parse(m.replace("%amount%", dFormat.format(amount)))));
    }

    public void loadMessages() {
        if (messageConfig.getConfigurationSection("boost-messages") == null)
            return;
        for (BoostType boostType : BoostType.values()) {
            String key = boostType.getConfigKey();
            if (messageConfig.getBoolean("boost-messages." + key + ".enabled"))
                messages.put(boostType, messageConfig.getStringList("boost-messages." + key + ".message"));
        }
    }

}
