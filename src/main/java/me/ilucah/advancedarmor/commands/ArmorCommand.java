package me.ilucah.advancedarmor.commands;

import me.ilucah.advancedarmor.handler.apimanager.event.ArmorGiveItemEvent;
import me.ilucah.advancedarmor.utilities.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.Armor;
import me.ilucah.advancedarmor.armor.ArmorType;

public class ArmorCommand implements CommandExecutor {

    private AdvancedArmor plugin;
    private MessageUtils messageUtils;

    public ArmorCommand(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.messageUtils = new MessageUtils(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("advancedarmor.command")) {
                if (args.length < 1) {
                    messageUtils.getConfigMessage("Command-Info").iterator().forEachRemaining(s -> {
                        p.sendMessage(s);
                    });
                    return true;
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("types")) {
                        if (p.hasPermission("advancedarmor.command.types")) {
                            messageUtils.getConfigMessage("Armor-Types-Message").iterator().forEachRemaining(s -> {
                                p.sendMessage(s);
                            });
                            for (Armor armorSet : plugin.getHandler().getArmor()) {
                                p.sendMessage(ChatColor.AQUA + armorSet.getName());
                            }
                            return true;
                        } else {
                            messageUtils.getConfigMessage("No-Permission").iterator().forEachRemaining(s -> {
                                p.sendMessage(s);
                            });
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (p.hasPermission("advancedarmor.command.reload")) {
                            messageUtils.getConfigMessage("Reload-Config").iterator().forEachRemaining(s -> {
                                p.sendMessage(s);
                            });
                            try {
                                plugin.reloadConfig();
                                plugin.getHandler().reloadCaches();
                            } catch (Exception e) {
                                messageUtils.getConfigMessage("Reload-Failed").iterator().forEachRemaining(s -> {
                                    s.replace("%issue-printout%", e.getMessage());
                                    p.sendMessage(s);
                                });
                            }
                            return true;
                        } else {
                            messageUtils.getConfigMessage("No-Permission").iterator().forEachRemaining(s -> {
                                p.sendMessage(s);
                            });
                            return true;
                        }
                    } else {
                        messageUtils.getConfigMessage("Armor-Correct-Usage").iterator().forEachRemaining(s -> {
                            p.sendMessage(s);
                        });
                        return true;
                    }
                } else if (args.length < 4) {
                    messageUtils.getConfigMessage("Armor-Correct-Usage").iterator().forEachRemaining(s -> {
                        p.sendMessage(s);
                    });
                    return true;
                } else if (args.length == 4) {
                    if (p.hasPermission("advancedarmor.command.give")) {
                        ArmorType type = ArmorType.HELMET;
                        if (args[3].equalsIgnoreCase("chestplate")) {
                            type = ArmorType.CHESTPLATE;
                        } else if (args[3].equalsIgnoreCase("leggings")) {
                            type = ArmorType.LEGGINGS;
                        } else if (args[3].equalsIgnoreCase("boots")) {
                            type = ArmorType.BOOTS;
                        } else if (args[3].equalsIgnoreCase("helmet")) {
                            type = ArmorType.HELMET;
                        } else {
                            messageUtils.getConfigMessage("Incorrect-Type-Of-Piece").iterator().forEachRemaining(s -> {
                                p.sendMessage(s);
                            });
                            return true;
                        }
                        Armor armor = null;
                        for (Armor armorType : plugin.getHandler().getArmor()) {
                            if (args[2].toLowerCase().contains(armorType.getName().toLowerCase())) {
                                armor = armorType;
                                break;
                            }
                        }
                        if (armor == null) {
                            messageUtils.getConfigMessage("Incorrect-Armor-Type").iterator().forEachRemaining(s -> {
                                p.sendMessage(s);
                            });
                            for (Armor armorSet : plugin.getHandler().getArmor()) {
                                p.sendMessage(ChatColor.AQUA + armorSet.getName());
                            }
                            return true;
                        }
                        try {
                            @SuppressWarnings("deprecation")
                            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                            if (player.isOnline()) {
                                ArmorGiveItemEvent event = new ArmorGiveItemEvent(player.getPlayer(), armor, armor.getItemStackFromType(type));
                                plugin.getServer().getPluginManager().callEvent(event);
                                player.getPlayer().getInventory().addItem(event.getItem());

                                Armor finalArmor = armor;
                                messageUtils.getConfigMessage("Sent-Player-Armor-Message").iterator().forEachRemaining(s -> {
                                    p.sendMessage(s.replace("{username}", player.getName()).replace("{armor_type}", finalArmor.getName()).replace("{armor_piece}", args[3].toUpperCase()));
                                });
                                messageUtils.getConfigMessage("Received-Armor-Message").iterator().forEachRemaining(s -> {
                                    player.getPlayer().sendMessage(s.replace("{username}", p.getName()).replace("{armor_type}", finalArmor.getName()).replace("{armor_piece}", args[3].toUpperCase()));
                                });
                                return true;
                            } else {
                                messageUtils.getConfigMessage("Player-Not-Online").iterator().forEachRemaining(s -> {
                                    p.sendMessage(s.replace("{username}", args[1]));
                                });
                                return true;
                            }
                        } catch (Exception e) {
                            messageUtils.getConfigMessage("Player-Does-Not-Exist").iterator().forEachRemaining(s -> {
                                p.sendMessage(s.replace("{username}", args[1]));
                            });
                            return true;
                        }
                    } else {
                        messageUtils.getConfigMessage("No-Permission").iterator().forEachRemaining(s -> {
                            p.sendMessage(s);
                        });
                        return true;
                    }
                } else {
                    messageUtils.getConfigMessage("Armor-Correct-Usage").iterator().forEachRemaining(s -> {
                        p.sendMessage(s);
                    });
                    return true;
                }
            } else {
                messageUtils.getConfigMessage("No-Permission").iterator().forEachRemaining(s -> {
                    p.sendMessage(s);
                });
                return true;
            }
        } else {
            CommandSender p = sender;
            if (args.length < 1) {
                messageUtils.getConfigMessage("Command-Info").iterator().forEachRemaining(s -> {
                    p.sendMessage(s);
                });
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("types")) {
                    messageUtils.getConfigMessage("Armor-Types-Message").iterator().forEachRemaining(s -> {
                        p.sendMessage(s);
                    });
                    for (Armor armorSet : plugin.getHandler().getArmor()) {
                        p.sendMessage(ChatColor.AQUA + armorSet.getName());
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    messageUtils.getConfigMessage("Reload-Config").iterator().forEachRemaining(s -> {
                        p.sendMessage(s);
                    });
                    try {
                        plugin.reloadConfig();
                        plugin.getHandler().initialiseArmor();
                        plugin.getHandler().initialiseColors();
                    } catch (Exception e) {
                        messageUtils.getConfigMessage("Reload-Failed").iterator().forEachRemaining(s -> {
                            s.replace("%issue-printout%", e.getMessage());
                            p.sendMessage(s);
                        });
                    }
                    return true;
                }
            } else if (args.length < 4) {
                messageUtils.getConfigMessage("Armor-Correct-Usage").iterator().forEachRemaining(s -> {
                    p.sendMessage(s);
                });
                return true;
            } else if (args.length == 4) {
                ArmorType type = ArmorType.HELMET;
                if (args[3].equalsIgnoreCase("chestplate")) {
                    type = ArmorType.CHESTPLATE;
                } else if (args[3].equalsIgnoreCase("leggings")) {
                    type = ArmorType.LEGGINGS;
                } else if (args[3].equalsIgnoreCase("boots")) {
                    type = ArmorType.BOOTS;
                } else if (args[3].equalsIgnoreCase("helmet")) {
                    type = ArmorType.HELMET;
                } else {
                    messageUtils.getConfigMessage("Incorrect-Type-Of-Piece").iterator().forEachRemaining(s -> {
                        p.sendMessage(s);
                    });
                    return true;
                }
                Armor armor = null;
                for (Armor armorType : plugin.getHandler().getArmor()) {
                    if (args[2].toLowerCase().contains(armorType.getName().toLowerCase())) {
                        armor = armorType;
                        break;
                    }
                }
                if (armor == null) {
                    messageUtils.getConfigMessage("Incorrect-Armor-Type").iterator().forEachRemaining(s -> {
                        p.sendMessage(s);
                    });
                    for (Armor armorSet : plugin.getHandler().getArmor()) {
                        p.sendMessage(ChatColor.AQUA + armorSet.getName());
                    }
                    return true;
                }
                try {
                    @SuppressWarnings("deprecation")
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    if (player.isOnline()) {
                        player.getPlayer().getInventory().addItem(armor.getItemStackFromType(type));

                        Armor finalArmor = armor;
                        messageUtils.getConfigMessage("Sent-Player-Armor-Message").iterator().forEachRemaining(s -> {
                            p.sendMessage(s.replace("{username}", player.getName()).replace("{armor_type}", finalArmor.getName()).replace("{armor_piece}", args[3].toUpperCase()));
                        });
                        messageUtils.getConfigMessage("Received-Armor-Message").iterator().forEachRemaining(s -> {
                            player.getPlayer().sendMessage(s.replace("{username}", p.getName()).replace("{armor_type}", finalArmor.getName()).replace("{armor_piece}", args[3].toUpperCase()));
                        });
                        return true;
                    } else {
                        messageUtils.getConfigMessage("Player-Not-Online").iterator().forEachRemaining(s -> {
                            p.sendMessage(s.replace("{username}", args[1]));
                        });
                        return true;
                    }
                } catch (Exception e) {
                    messageUtils.getConfigMessage("Player-Does-Not-Exist").iterator().forEachRemaining(s -> {
                        p.sendMessage(s.replace("{username}", args[1]));
                    });
                    return true;
                }
            } else {
                messageUtils.getConfigMessage("Armor-Correct-Usage").iterator().forEachRemaining(s -> {
                    p.sendMessage(s);
                });
                return true;
            }
        }
        return false;
    }
}