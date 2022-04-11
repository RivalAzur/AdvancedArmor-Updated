package me.ilucah.advancedarmor.utilities;

import com.iridium.iridiumcolorapi.IridiumColorAPI;

public class RGBParser {

    public static String parse(String str) {
        return IridiumColorAPI.process(str);
    }

}