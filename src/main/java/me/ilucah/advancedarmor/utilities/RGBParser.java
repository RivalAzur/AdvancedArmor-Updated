package me.ilucah.advancedarmor.utilities;

import com.iridium.iridiumcolorapi.IridiumColorAPI;

public class RGBParser {

    public static String parse(String str) {
        return IridiumColorAPI.process(str);
    }

    public static String parseHelmetTitle(String str) {
        str.replace("%armor_type%", "Helmet");
        return IridiumColorAPI.process(str);
    }

    public static String parseChestplateTitle(String str) {
        str.replace("%armor_type%", "Chestplate");
        return IridiumColorAPI.process(str);
    }

    public static String parseLeggingsTitle(String str) {
        str.replace("%armor_type%", "Leggings");
        return IridiumColorAPI.process(str);
    }

    public static String parseBootsTitle(String str) {
        str.replace("%armor_type%", "Boots");
        return IridiumColorAPI.process(str);
    }
}