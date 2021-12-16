package me.ilucah.advancedarmor.handler.apimanager;

import me.ilucah.advancedarmor.handler.Handler;
import me.ilucah.advancedarmor.utilities.MoneyUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyPlayer {

    private Handler handler;
    private Player player;

    public MoneyPlayer(Handler handler, Player player) {
        this.handler = handler;
        this.player = player;
    }

    public MoneyPlayer(Handler handler, OfflinePlayer offlinePlayer) {
        this.handler = handler;
        this.player = offlinePlayer.getPlayer();
    }

    public MoneyPlayer(Handler handler, UUID uuid) {
        this.handler = handler;
        this.player = Bukkit.getPlayer(uuid);
    }

    public MoneyPlayer(Handler handler, String playerName) {
        this.handler = handler;
        this.player = Bukkit.getPlayer(playerName);
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
}
