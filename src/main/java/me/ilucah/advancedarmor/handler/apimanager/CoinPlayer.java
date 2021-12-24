package me.ilucah.advancedarmor.handler.apimanager;

import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CoinPlayer {

    private final Player player;
    private final Handler handler;
    private final NBTUtils nbtUtils;

    public CoinPlayer(Handler handler, Player player) {
        this.player = player;
        this.handler = handler;
        this.nbtUtils = new NBTUtils(handler);
    }

    public CoinPlayer(Handler handler, OfflinePlayer offlinePlayer) {
        this.player = offlinePlayer.getPlayer();
        this.handler = handler;
        this.nbtUtils = new NBTUtils(handler);
    }

    public CoinPlayer(Handler handler, String stringPlayer) {
        this.player = Bukkit.getPlayer(stringPlayer);
        this.handler = handler;
        this.nbtUtils = new NBTUtils(handler);
    }

    public CoinPlayer(Handler handler, UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.handler = handler;
        this.nbtUtils = new NBTUtils(handler);
    }

    public boolean hasCustomArmorEquipped() {
        if (nbtUtils.hasArmorNBTTag(player.getInventory().getBoots())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getBoots())).getBoostType() == BoostType.COIN)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getChestplate())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getChestplate())).getBoostType() == BoostType.COIN)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getLeggings())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getLeggings())).getBoostType() == BoostType.COIN)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getHelmet())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getHelmet())).getBoostType() == BoostType.COIN)
            return true;
        else
            return false;
    }

}