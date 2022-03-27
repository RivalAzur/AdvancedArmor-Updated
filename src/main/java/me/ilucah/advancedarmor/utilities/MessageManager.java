package me.ilucah.advancedarmor.utilities;

import me.ilucah.advancedarmor.AdvancedArmor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MessageManager {

    private final AdvancedArmor plugin;
    private FileConfiguration config;

    private List<String> coinMessage, expMessage, moneyMessage, tokenMessage, gemMessage;
    private boolean coinIsEnabled, expIsEnabled, moneyIsEnabled, tokenIsEnabled, gemIsEnabled;

    public MessageManager(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        initStringValues();
        initBoolValues();
    }

    private void initStringValues() {
        coinMessage = config.getStringList("Messages.BoostMessages.Coins.Message");
        expMessage = config.getStringList("Messages.BoostMessages.EXP.Message");
        moneyMessage = config.getStringList("Messages.BoostMessages.Money.Message");
        tokenMessage = config.getStringList("Messages.BoostMessages.Tokens.Message");
        gemMessage = config.getStringList("Messages.BoostMessages.Gems.Message");
    }

    private void initBoolValues() {
        coinIsEnabled = config.getBoolean("Messages.BoostMessages.Coins.Enabled");
        expIsEnabled = config.getBoolean("Messages.BoostMessages.EXP.Enabled");
        moneyIsEnabled = config.getBoolean("Messages.BoostMessages.Money.Enabled");
        tokenIsEnabled = config.getBoolean("Messages.BoostMessages.Tokens.Enabled");
        gemIsEnabled = config.getBoolean("Messages.BoostMessages.Gems.Enabled");
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public List<String> getCoinMessage() {
        return coinMessage;
    }

    public void setCoinMessage(List<String> coinMessage) {
        this.coinMessage = coinMessage;
    }

    public List<String> getExpMessage() {
        return expMessage;
    }

    public void setExpMessage(List<String> expMessage) {
        this.expMessage = expMessage;
    }

    public List<String> getMoneyMessage() {
        return moneyMessage;
    }

    public void setMoneyMessage(List<String> moneyMessage) {
        this.moneyMessage = moneyMessage;
    }

    public List<String> getTokenMessage() {
        return tokenMessage;
    }

    public void setTokenMessage(List<String> tokenMessage) {
        this.tokenMessage = tokenMessage;
    }

    public boolean isCoinIsEnabled() {
        return coinIsEnabled;
    }

    public void setCoinIsEnabled(boolean coinIsEnabled) {
        this.coinIsEnabled = coinIsEnabled;
    }

    public boolean isExpIsEnabled() {
        return expIsEnabled;
    }

    public void setExpIsEnabled(boolean expIsEnabled) {
        this.expIsEnabled = expIsEnabled;
    }

    public boolean isMoneyIsEnabled() {
        return moneyIsEnabled;
    }

    public void setMoneyIsEnabled(boolean moneyIsEnabled) {
        this.moneyIsEnabled = moneyIsEnabled;
    }

    public boolean isTokenIsEnabled() {
        return tokenIsEnabled;
    }

    public void setTokenIsEnabled(boolean tokenIsEnabled) {
        this.tokenIsEnabled = tokenIsEnabled;
    }

    public boolean isGemIsEnabled() {
        return gemIsEnabled;
    }

    public void setGemIsEnabled(boolean gemIsEnabled) {
        this.gemIsEnabled = gemIsEnabled;
    }

    public List<String> getGemMessage() {
        return gemMessage;
    }

    public void setGemMessage(List<String> gemMessage) {
        this.gemMessage = gemMessage;
    }
}
