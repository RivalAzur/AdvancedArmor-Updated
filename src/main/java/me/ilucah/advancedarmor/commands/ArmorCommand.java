package me.ilucah.advancedarmor.commands;

import com.iridium.iridiumcolorapi.*;
import de.tr7zw.nbtapi.*;
import me.ilucah.advancedarmor.api.events.ArmorGiveItemEvent;
import me.ilucah.advancedarmor.listener.*;
import me.ilucah.advancedarmor.utilities.*;
import me.ilucah.advancedarmor.utilities.msg.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import me.ilucah.advancedarmor.AdvancedArmor;
import me.ilucah.advancedarmor.armor.Armor;
import me.ilucah.advancedarmor.armor.ArmorType;
import org.bukkit.inventory.*;
import org.bukkit.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ArmorCommand implements TabExecutor {

    private AdvancedArmor plugin;
    private MessageUtils messageUtils;

    public ArmorCommand(AdvancedArmor plugin) {
        this.plugin = plugin;
        this.messageUtils = new MessageUtils(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender.hasPermission("advancedarmor.command")) {
            if (args.length < 1) {
                messageUtils.getConfigMessage("Command-Info").iterator().forEachRemaining(s -> sender.sendMessage(s));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("types")) {
                    if (sender.hasPermission("advancedarmor.command.types")) {
                        messageUtils.getConfigMessage("Armor-Types-Message").iterator().forEachRemaining(s -> sender.sendMessage(s));
                        for (Armor armorSet : plugin.getHandler().getArmor()) {
                            sender.sendMessage(IridiumColorAPI.process(" &3* &b" + armorSet.getName()));
                        }
                        return true;
                    } else {
                        messageUtils.getConfigMessage("No-Permission").iterator().forEachRemaining(s -> {
                            sender.sendMessage(s);
                        });
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("advancedarmor.command.reload")) {
                        messageUtils.getConfigMessage("Reload-Config").iterator().forEachRemaining(s -> {
                            sender.sendMessage(s);
                        });
                        try {
                            plugin.getConfigManager().load();
                            plugin.getHandler().reloadCaches();
                            ShardListener.reloadShards();
                            ShardListener.reloadArmor();
                        } catch (Exception e) {
                            messageUtils.getConfigMessage("Reload-Failed").iterator().forEachRemaining(s -> {
                                sender.sendMessage(s.replace("%issue-printout%", e.getMessage()));
                            });
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        messageUtils.getConfigMessage("No-Permission").iterator().forEachRemaining(s -> {
                            sender.sendMessage(s);
                        });
                        return true;
                    }
                } else {
                    messageUtils.getConfigMessage("Armor-Correct-Usage").iterator().forEachRemaining(s -> {
                        sender.sendMessage(s);
                    });
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("giveset") && args.length == 3) {
                Armor armor = null;
                for (Armor armorType : plugin.getHandler().getArmor()) {
                    if (args[2].toLowerCase().contains(armorType.getName().toLowerCase())) {
                        armor = armorType;
                        break;
                    }
                }
                if (armor == null) {
                    messageUtils.getConfigMessage("Incorrect-Armor-Type").iterator().forEachRemaining(s -> {
                        sender.sendMessage(s);
                    });
                    for (Armor armorSet : plugin.getHandler().getArmor()) {
                        sender.sendMessage(IridiumColorAPI.process(" &3* &b" + armorSet.getName()));
                    }
                    return true;
                }
                try {
                    @SuppressWarnings("deprecation")
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    if (player.isOnline()) {
                        player.getPlayer().getInventory().addItem(armor.getItemStackFromType(ArmorType.HELMET));
                        player.getPlayer().getInventory().addItem(armor.getItemStackFromType(ArmorType.CHESTPLATE));
                        player.getPlayer().getInventory().addItem(armor.getItemStackFromType(ArmorType.LEGGINGS));
                        player.getPlayer().getInventory().addItem(armor.getItemStackFromType(ArmorType.BOOTS));
                        return true;
                    } else {
                        messageUtils.getConfigMessage("Player-Not-Online").iterator().forEachRemaining(s -> {
                            sender.sendMessage(s.replace("{username}", args[1]));
                        });
                        return true;
                    }
                } catch (Exception e) {
                    messageUtils.getConfigMessage("Player-Does-Not-Exist").iterator().forEachRemaining(s -> {
                        sender.sendMessage(s.replace("{username}", args[1]));
                    });
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("giveshards") && args.length  <= 4) {
                if (sender.hasPermission("advancedarmor.command.giveshards")) {
                    if (ShardListener.shardMap.containsKey(args[2])) {
                        String shard = args[2];
                        int amount = 1;
                        if (args.length == 4){
                            amount = Integer.parseInt(args[3]);
                        }
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player != null) {
                            NBTItem nbtItem = new NBTItem(ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Shards." + shard + ".shard-item")).build(), true);
                            nbtItem.setBoolean("ArmorShard", Boolean.TRUE);
                            ItemStack itemStack = nbtItem.getItem();
                            itemStack.setAmount(amount);
                            player.getInventory().addItem(itemStack);
                        }else {
                            messageUtils.getConfigMessage("Player-Not-Online").iterator().forEachRemaining(s -> {
                                sender.sendMessage(s.replace("{username}", args[1]));
                            });
                            return true;
                        }

                    } else {
                        messageUtils.getConfigMessage("Incorrect-Type-Of-Piece").iterator().forEachRemaining(s -> {
                            sender.sendMessage(s);
                        });
                        return true;
                    }
                }
            } else if (args[0].equalsIgnoreCase("give") && args.length == 4) {
                if (sender.hasPermission("advancedarmor.command.give")) {
                    ArmorType type;
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
                            sender.sendMessage(s);
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
                            sender.sendMessage(s);
                        });
                        for (Armor armorSet : plugin.getHandler().getArmor()) {
                            sender.sendMessage(IridiumColorAPI.process(" &3* &b" + armorSet.getName()));
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
                                sender.sendMessage(s.replace("{username}", player.getName()).replace("{armor_type}", finalArmor.getName()).replace("{armor_piece}", ShardListener.getArmorType(event.getItem().getType())));
                            });
                            messageUtils.getConfigMessage("Received-Armor-Message").iterator().forEachRemaining(s -> {
                                player.getPlayer().sendMessage(s.replace("{username}", sender instanceof ConsoleCommandSender ? "Console" : sender.getName() ).replace("{armor_type}", finalArmor.getName()).replace("{armor_piece}", ShardListener.getArmorType(event.getItem().getType())));
                            });
                            return true;
                        } else {
                            messageUtils.getConfigMessage("Player-Not-Online").iterator().forEachRemaining(s -> {
                                sender.sendMessage(s.replace("{username}", args[1]));
                            });
                            return true;
                        }
                    } catch (Exception e) {
                        messageUtils.getConfigMessage("Player-Does-Not-Exist").iterator().forEachRemaining(s -> {
                            sender.sendMessage(s.replace("{username}", args[1]));
                        });
                        return true;
                    }
                } else {
                    messageUtils.getConfigMessage("No-Permission").iterator().forEachRemaining(s -> {
                        sender.sendMessage(s);
                    });
                    return true;
                }
            } else {
                messageUtils.getConfigMessage("Armor-Correct-Usage").iterator().forEachRemaining(s -> {
                    sender.sendMessage(s);
                });
                return true;
            }
        } else {
            messageUtils.getConfigMessage("No-Permission").iterator().forEachRemaining(s -> {
                sender.sendMessage(s);
            });
            return true;
        }
        return true;
    }



    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            if (command.getName().equalsIgnoreCase("armor")){
                if (sender.hasPermission("advancedarmor.command")) {
                    if (args.length == 1) {
                        return Arrays.asList("give", "reload", "types", "giveshards", "giveset");
                    }
                    if (args[0].equals("give")) {
                        List<String> list = new ArrayList<>();
                        List<String> suggestions = new ArrayList<>();
                        if (args.length == 2) {
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                list.add(p.getName());
                            }
                            return StringUtil.copyPartialMatches(args[1], list, suggestions);
                        }
                            if (args.length == 3) {
                                ArrayList armorTypes = new ArrayList();
                                for (Armor armorSet : plugin.getHandler().getArmor()) {
                                    armorTypes.add(armorSet.getName());
                                }
                                return StringUtil.copyPartialMatches(args[2], armorTypes, new ArrayList<>());
                            }
                        if (args.length == 4) {
                            return StringUtil.copyPartialMatches(args[3], Arrays.asList("helmet","chestplate", "leggings" , "boots"), new ArrayList<>());
                        }
                            else {
                                return Collections.emptyList();
                            }

                        }

                    if (args[0].equals("giveset")) {
                        List<String> list = new ArrayList<>();
                        List<String> suggestions = new ArrayList<>();
                        if (args.length == 2) {
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                list.add(p.getName());
                            }
                            return StringUtil.copyPartialMatches(args[1], list, suggestions);
                        }
                        if (args.length == 3) {
                            ArrayList armorTypes = new ArrayList();
                            for (Armor armorSet : plugin.getHandler().getArmor()) {
                                armorTypes.add(armorSet.getName());
                            }
                            return StringUtil.copyPartialMatches(args[2], armorTypes, new ArrayList<>());
                        }
                        else {
                            return Collections.emptyList();
                        }

                    }

                    if (args[0].equals("giveshards")) {
                        List<String> list = new ArrayList<>();
                        List<String> suggestions = new ArrayList<>();
                        if (args.length == 2) {
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                list.add(p.getName());
                            }
                            return StringUtil.copyPartialMatches(args[1], list, suggestions);
                        }
                        if (args.length == 3) {
                            List<String> shardKeys = new ArrayList<>(ShardListener.shardMap.keySet());
                            return StringUtil.copyPartialMatches(args[2],shardKeys , new ArrayList<>());
                        }
                        else {
                            return Collections.emptyList();
                        }

                    }

                    }

                }



        return null;
    }
}