package me.ilucah.advancedarmor.utilities;

import me.ilucah.advancedarmor.AdvancedArmor;

import java.util.ArrayList;
import java.util.List;

public class MessageUtils {

    private AdvancedArmor main;

    public MessageUtils(AdvancedArmor main) {
        this.main = main;
    }

    public List<String> getConfigMessage(String path) {
        List<String> message = new ArrayList<>();
        main.getConfigManager().getMessages().getStringList("Messages." + path).forEach(str -> message.add(RGBParser.parse(str)));
        return message;
    }

    public String getServerName() {
        return main.getConfig().getString("Messages.Console-Name");
    }
}
