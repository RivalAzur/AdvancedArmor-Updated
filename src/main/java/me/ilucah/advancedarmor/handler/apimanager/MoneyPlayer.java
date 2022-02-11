package me.ilucah.advancedarmor.handler.apimanager;

import me.ilucah.advancedarmor.armor.BoostType;
import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import me.ilucah.advancedarmor.utilities.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyPlayer {

    private Handler handler;
    private Player player;
    private NBTUtils nbtUtils;

    public MoneyPlayer(Handler handler, Player player) {
        this.handler = handler;
        this.player = player;
        this.nbtUtils = new NBTUtils(handler);
    }

    public MoneyPlayer(Handler handler, OfflinePlayer offlinePlayer) {
        this.handler = handler;
        this.player = offlinePlayer.getPlayer();
        this.nbtUtils = new NBTUtils(handler);
    }

    public MoneyPlayer(Handler handler, UUID uuid) {
        this.handler = handler;
        this.player = Bukkit.getPlayer(uuid);
        this.nbtUtils = new NBTUtils(handler);
    }

    public MoneyPlayer(Handler handler, String playerName) {
        this.handler = handler;
        this.player = Bukkit.getPlayer(playerName);
        this.nbtUtils = new NBTUtils(handler);
    }

    public Player getPlayer() {
        return player;
    }

//    public double getPlayerBalance() throws UserDoesNotExistException {
//        return Economy.getMoney(player.getName());
//    }
//
//    public void setMoney(BigDecimal amount) throws MaxMoneyException, UserDoesNotExistException, NoLoanPermittedException {
//        Economy.setMoney(player.getUniqueId(), amount);
//    }
//
//    public void addMoney(BigDecimal amount) throws MaxMoneyException, UserDoesNotExistException, NoLoanPermittedException {
//        Economy.add(player.getUniqueId(), amount);
//    }
//
//    public void removeMoney(BigDecimal amount) throws MaxMoneyException, UserDoesNotExistException, NoLoanPermittedException {
//        Economy.subtract(player.getUniqueId(), amount);
//    }

    public double getRawBoostAmount() {
        MoneyUtils moneyUtils = new MoneyUtils(handler);
        return moneyUtils.calculateRawArmorMultiPercentage(player.getInventory().getHelmet(),
                player.getInventory().getChestplate(),
                player.getInventory().getLeggings(),
                player.getInventory().getBoots());
    }

    public boolean hasCustomArmorEquipped() {
        if (nbtUtils.hasArmorNBTTag(player.getInventory().getBoots())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getBoots())).getBoostType() == BoostType.MONEY)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getChestplate())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getChestplate())).getBoostType() == BoostType.MONEY)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getLeggings())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getLeggings())).getBoostType() == BoostType.MONEY)
            return true;
        else if (nbtUtils.hasArmorNBTTag(player.getInventory().getHelmet())
                && handler.getArmorFromString(nbtUtils.getArmorName(player.getInventory().getHelmet())).getBoostType() == BoostType.MONEY)
            return true;
        else
            return false;
    }
}
