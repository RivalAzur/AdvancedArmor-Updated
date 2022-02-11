package me.ilucah.advancedarmor.handler.apimanager;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.NBTUtils;
import me.ilucah.advancedarmor.utilities.TokenUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TokenPlayer {

    private final Player player;
    private final Handler handler;
    private final NBTUtils nbtUtils;

    public TokenPlayer(Handler handler, Player player) {
        this.player = player;
        this.handler = handler;
        this.nbtUtils = new NBTUtils(handler);
    }

    public TokenPlayer(Handler handler, OfflinePlayer offlinePlayer) {
        this.player = offlinePlayer.getPlayer();
        this.handler = handler;
        this.nbtUtils = new NBTUtils(handler);
    }

    public TokenPlayer(Handler handler, String stringPlayer) {
        this.player = Bukkit.getPlayer(stringPlayer);
        this.handler = handler;
        this.nbtUtils = new NBTUtils(handler);
    }

    public TokenPlayer(Handler handler, UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.handler = handler;
        this.nbtUtils = new NBTUtils(handler);
    }

    public boolean hasCustomArmorEquipped() {
        if (nbtUtils.hasArmorNBTTag(player.getInventory().getBoots())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getBoots())).getBoostType() == BoostType.TOKEN)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getChestplate())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getChestplate())).getBoostType() == BoostType.TOKEN)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getLeggings())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getLeggings())).getBoostType() == BoostType.TOKEN)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getHelmet())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getHelmet())).getBoostType() == BoostType.TOKEN)
            return true;
        else
            return false;
    }

    public int getRawPlayerArmorExpBoost() {
        TokenUtils tokenUtils = new TokenUtils(handler);
        return tokenUtils.calculateRawArmorMultiPercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(), player.getInventory().getLeggings(),
                player.getInventory().getBoots());
    }
}