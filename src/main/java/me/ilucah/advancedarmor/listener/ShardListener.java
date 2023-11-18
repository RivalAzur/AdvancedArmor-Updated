package me.ilucah.advancedarmor.listener;

import com.iridium.iridiumcolorapi.*;
import de.tr7zw.nbtapi.*;
import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.api.*;
import me.ilucah.advancedarmor.armor.*;
import me.ilucah.advancedarmor.armor.lib.*;
import me.ilucah.advancedarmor.boosting.model.*;
import me.ilucah.advancedarmor.ui.*;
import me.ilucah.advancedarmor.utilities.*;
import me.ilucah.advancedarmor.utilities.nbt.*;
import me.ilucah.advancedarmor.utilities.xutils.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.scheduler.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class ShardListener implements Listener {
    public static List<Armor> purchasableArmor;
    public static Map<String, ShardObj> shardMap;

    public static Map<EntityType, Map<ShardObj, Double>> entityShardMap;
    public static Map<Material, Map<ShardObj, Double>> blockShardMap;
    public static List<Armor> getPurchasableArmor() {
        return purchasableArmor;
    }
    public ShardListener(){
        this.purchasableArmor = new ArrayList<>();
        this.blockShardMap = new HashMap<>();
        this.entityShardMap = new HashMap<>();
        this.shardMap = new HashMap<>();
        if (Bukkit.getPluginManager().getPlugin("RivalHarvesterHoe") != null){
            Bukkit.getPluginManager().registerEvents(new RivalHoesListener(),AdvancedArmor.instance);
        }
        if (Bukkit.getPluginManager().getPlugin("RivalPickaxes") != null){
            Bukkit.getPluginManager().registerEvents(new RivalPickaxesListener(),AdvancedArmor.instance);
        }
        loadShards();
        reloadPArmor();
    }

    private static void loadShards() {
        FileConfiguration configuration = AdvancedArmor.instance.getConfigManager().getShards();
        if (configuration.getConfigurationSection("Shards").getKeys(false) == null)
            return;
        for (String shardType : configuration.getConfigurationSection("Shards").getKeys(false)) {
            String armorTypeName = configuration.getString("Shards." + shardType + ".Armor-Type");
            ArrayList<String> mobArray = new ArrayList<>();
            if (configuration.contains("Shards." + shardType + ".Mobs")) {
                for (String mob : configuration.getStringList("Shards." + shardType + ".Mobs")) {
                    mobArray.add(mob);
                }
            }
            ArrayList<String> blockArray = new ArrayList<>();
            if (configuration.contains("Shards." + shardType + ".Blocks")) {
                for (String blocks : configuration.getStringList("Shards." + shardType + ".Blocks")) {
                    blockArray.add(blocks);
                }
            }
            int helmetPrice = configuration.getInt("Shards." + shardType + ".helmet-price");
            int chestplatePrice = configuration.getInt("Shards." + shardType + ".chestplate-price");
            int leggingsPrice = configuration.getInt("Shards." + shardType + ".leggings-price");
            int bootsPrice = configuration.getInt("Shards." + shardType + ".boots-price");
            int menuSlot = configuration.getInt("Shards." + shardType + ".click-to-select.slot");
            ShardObj shard = new ShardObj(shardType, armorTypeName,helmetPrice,chestplatePrice,leggingsPrice,bootsPrice, menuSlot);
            shardMap.put(shardType, shard);
            for (String mob : mobArray) {
                String[] mobSplit = mob.split(":");
                if (!entityShardMap.containsKey(EntityType.valueOf(mobSplit[0])))
                    entityShardMap.put(EntityType.valueOf((mobSplit[0])), new HashMap<>());
                entityShardMap.get(EntityType.valueOf((mobSplit[0]))).put(shard, Double.valueOf(mobSplit[1]));
            }

            for (String block : blockArray) {
                String[] blockSplit = block.split(":");
                if (!blockShardMap.containsKey(Material.valueOf(blockSplit[0])))
                    blockShardMap.put(Material.valueOf((blockSplit[0])), new HashMap<>());
                blockShardMap.get(Material.valueOf((blockSplit[0]))).put(shard, Double.valueOf(blockSplit[1]));
            }

        }
    }

    public static void reloadArmor() {
       purchasableArmor.clear();
        reloadPArmor();
    }

    public static void reloadShards() {
        shardMap.clear();
        loadShards();
    }


    @Nullable
    public static ShardObj getShardFromArmorName(String armorName) {
        for (ShardObj shard : shardMap.values()) {
            if (shard.getArmorTypeName().equalsIgnoreCase(armorName))
                return shard;
        }
        return null;
    }


    public Map<ShardObj, Double> getShardFromMob(EntityType entityType) {
        if (!this.entityShardMap.containsKey(entityType))
            this.entityShardMap.put(entityType, new HashMap<>());
        return this.entityShardMap.get(entityType);
    }
    public static Map<ShardObj, Double> getShardFromMaterial(Material material) {
        if (!blockShardMap.containsKey(material))
            blockShardMap.put(material, new HashMap<>());
        return blockShardMap.get(material);
    }

    private static void reloadPArmor() {
        for (String armorType : AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shop.armor-types")) {
            if (AdvancedArmor.instance.getHandler().getArmorFromString(armorType) == null) {
                continue;
            }
            purchasableArmor.add(AdvancedArmor.instance.getHandler().getArmorFromString(armorType));
        }
    }

    public boolean checkInventory(Player player, ItemStack itemStack, int amount) {
        int total = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR)
                if (item.isSimilar(itemStack))
                    total += item.getAmount();
        }
        if (total < amount)
            return false;
        ItemStack removeItem = itemStack.clone();
        removeItem.setAmount(amount);
        player.getInventory().removeItem(new ItemStack[] { removeItem });
        return true;
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if(event.getWhoClicked().getOpenInventory().getTopInventory() == null) return;
        if (event.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof ShopUI){
            event.setCancelled(true);
            if (event.getCurrentItem() == null)
                return;
            Player player = (Player) event.getWhoClicked();
            ShopUI shopUI = (ShopUI) event.getWhoClicked().getOpenInventory().getTopInventory().getHolder();
            if (NBTUtils.hasArmorNBTTag(event.getCurrentItem())) {
                Armor armor = AdvancedArmor.instance.getHandler().getArmorFromString(NBTUtils.getArmorName(event.getCurrentItem()));
                ShardObj shard = getShardFromArmorName(armor.getName());
                NBTItem nbtItem = new NBTItem( ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Shards." + shard.getShardTypeName() + ".shard-item")).build(),true);
                nbtItem.setBoolean("ArmorShard", Boolean.TRUE);
                if (event.getSlot() == shopUI.helmetSlot) {
                    if (checkInventory(player,nbtItem.getItem(),shard.getHelmetPrice())) {
                        event.getWhoClicked().getInventory().addItem(new ItemStack[] { armor.getHelmet() });
                        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 1.0F, 1.0F);
                        player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Messages.purchased").replace("%amount%", shard.getHelmetPrice()+ "").replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", armor.getName()).replace("%armor_type_piece%", "Helmet")));
                        return;
                    }
                    player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Messages.not-enough").replace("%amount%", shard.getHelmetPrice()+ "").replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", armor.getName()).replace("%armor_type_piece%", "Helmet")));
                    player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_BREAK.parseSound(), 1.0F, 1.0F);
                    shopUI.setArmorType(armor.getName());
                    event.getClickedInventory().setItem(shopUI.helmetSlot, ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Menu.not-enough-item")).build());
                    (new BukkitRunnable() {
                        public void run() {

                           shopUI.init();
                        }
                    }).runTaskLater(AdvancedArmor.instance, 40L);
                    return;
                }
                if (event.getSlot() == shopUI.chestplateSlot) {
                    if (checkInventory(player,nbtItem.getItem(),shard.getChestplatePrice())) {
                        event.getWhoClicked().getInventory().addItem(new ItemStack[] { armor.getChestplate() });
                        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 1.0F, 1.0F);
                        player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Messages.purchased").replace("%amount%", shard.getChestplatePrice()+ "").replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", armor.getName()).replace("%armor_type_piece%", "Chestplate")));
                        return;
                    }

                    player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Messages.not-enough").replace("%amount%", shard.getHelmetPrice()+ "").replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", armor.getName()).replace("%armor_type_piece%", "Helmet")));
                    player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_BREAK.parseSound(), 1.0F, 1.0F);
                    event.getClickedInventory().setItem(shopUI.chestplateSlot, ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Menu.not-enough-item")).build());
                    shopUI.setArmorType(armor.getName());
                    (new BukkitRunnable() {
                        public void run() {
                            shopUI.init();
                        }
                    }).runTaskLater(AdvancedArmor.instance, 40L);
                    return;
                }

                if (event.getSlot() == shopUI.leggingsSlot) {
                    if (checkInventory(player,nbtItem.getItem(),shard.getLeggingsPrice())) {
                        event.getWhoClicked().getInventory().addItem(new ItemStack[] { armor.getLeggings() });
                        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 1.0F, 1.0F);
                        player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Messages.purchased").replace("%amount%", shard.getLeggingsPrice()+ "").replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", armor.getName()).replace("%armor_type_piece%", "Leggings")));
                        return;
                    }
                    shopUI.setArmorType(armor.getName());
                    player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Messages.not-enough").replace("%amount%", shard.getHelmetPrice()+ "").replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", armor.getName()).replace("%armor_type_piece%", "Helmet")));
                    player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_BREAK.parseSound(), 1.0F, 1.0F);
                    event.getClickedInventory().setItem(shopUI.leggingsSlot, ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Menu.not-enough-item")).build());
                    (new BukkitRunnable() {
                        public void run() {
                            shopUI.init();
                        }
                    }).runTaskLater(AdvancedArmor.instance, 40L);
                    return;
                }
                if (event.getSlot() == shopUI.bootsSlot) {
                    if (checkInventory(player,nbtItem.getItem(),shard.getBootsPrice())) {
                        event.getWhoClicked().getInventory().addItem(new ItemStack[] { armor.getHelmet() });
                        player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 1.0F, 1.0F);
                        player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Messages.purchased").replace("%amount%", shard.getBootsPrice()+ "").replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", armor.getName()).replace("%armor_type_piece%", "Boots")));
                        return;
                    }
                    shopUI.setArmorType(armor.getName());
                    player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Messages.not-enough").replace("%amount%", shard.getHelmetPrice()+ "").replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", armor.getName()).replace("%armor_type_piece%", "Helmet")));
                    player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_BREAK.parseSound(), 1.0F, 1.0F);
                    event.getClickedInventory().setItem(shopUI.bootsSlot, ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Menu.not-enough-item")).build());
                    (new BukkitRunnable() {
                        public void run() {
                            shopUI.init();
                        }
                    }).runTaskLater(AdvancedArmor.instance, 40L);
                    return;
                }
                shopUI.setArmorType(armor.getName());
                String armorName = armor.getName();
                ItemStack helmet = armor.getHelmet().clone();
                ItemMeta helmetMeta = helmet.getItemMeta();
                List<String> helmetLore = helmetMeta.hasLore() ? helmetMeta.getLore() : new ArrayList<>();
                helmetLore.add(" ");
                AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards."+shard.getShardTypeName() +".piece-addon-lore").forEach(l -> helmetLore.add(l.replace("%cost%", String.valueOf(getShardFromArmorName(armorName).getHelmetPrice()))));
                helmetMeta.setLore(IridiumColorAPI.process(helmetLore));
                helmet.setItemMeta(helmetMeta);
                event.getInventory().setItem(shopUI.helmetSlot, helmet);

                // Chestplate
                ItemStack chestplate = armor.getChestplate().clone();
                ItemMeta chestplateItemMeta = chestplate.getItemMeta();
                List<String> chestplateLore = chestplateItemMeta.hasLore() ? chestplateItemMeta.getLore() : new ArrayList<>();
                chestplateLore.add(" ");
                AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards."+shard.getShardTypeName() +".piece-addon-lore").forEach(l -> chestplateLore.add(l.replace("%cost%", String.valueOf(getShardFromArmorName(armorName).getChestplatePrice()))));
                chestplateItemMeta.setLore(IridiumColorAPI.process(chestplateLore));
                chestplate.setItemMeta(chestplateItemMeta);
                event.getInventory().setItem(shopUI.chestplateSlot, chestplate);

// Leggings
                ItemStack leggings = armor.getLeggings().clone();
                ItemMeta leggingsItemMeta = leggings.getItemMeta();
                List<String> leggingsLore = leggingsItemMeta.hasLore() ? leggingsItemMeta.getLore() : new ArrayList<>();
                leggingsLore.add(" ");
                AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards."+shard.getShardTypeName() +".piece-addon-lore").forEach(l -> leggingsLore.add(l.replace("%cost%", String.valueOf(getShardFromArmorName(armorName).getLeggingsPrice()))));
                leggingsItemMeta.setLore(IridiumColorAPI.process(leggingsLore));
                leggings.setItemMeta(leggingsItemMeta);
                event.getInventory().setItem(shopUI.leggingsSlot, leggings);

// Boots
                ItemStack boots = armor.getBoots().clone();
                ItemMeta bootMeta = boots.getItemMeta();
                List<String> bootLore = bootMeta.hasLore() ? bootMeta.getLore() : new ArrayList<>();
                bootLore.add(" ");
                AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards."+shard.getShardTypeName() +".piece-addon-lore").forEach(l -> bootLore.add(l.replace("%cost%", String.valueOf(getShardFromArmorName(armorName).getBootsPrice()))));
                bootMeta.setLore(IridiumColorAPI.process(bootLore));
                boots.setItemMeta(bootMeta);
                event.getInventory().setItem(shopUI.bootsSlot, boots);

                player.playSound(player.getLocation(), XSound.ITEM_FLINTANDSTEEL_USE.parseSound(), 0.8F, 1.2F);
            }





        }
        }

    @EventHandler
    public void onInteract(BlockPlaceEvent event) {
        if (event.getItemInHand() == null)
            return;
        if (event.getBlockPlaced() == null || event.getBlock() == null || event.getBlockAgainst() == null)
            return;
        NBTItem item = new NBTItem(event.getItemInHand());
        if (item.hasKey("ArmorShard"))
            event.setCancelled(true);
    }

    public static String getArmorType(Material material) {
        switch (material) {
            case PLAYER_HEAD:
            case LEATHER_HELMET:
                return "Helmet";

            case LEATHER_CHESTPLATE:
                return "Chestplate";

            case LEATHER_LEGGINGS:
                return "Leggings";

            case LEATHER_BOOTS:
                return "Boots";

            default:
                return "UNKNOWN_ARMOR_TYPE";
        }
    }
    @EventHandler
    public void onEquip(final ArmorEquipEvent event) {

        final Player player = event.getPlayer();
        final ItemStack oldPiece = event.getOldArmorPiece();
        final ItemStack newPiece = event.getNewArmorPiece();

        if (oldPiece != null) {
            if (oldPiece.getType() != Material.AIR) {
                NBTItem oldPieceNBT = new NBTItem(oldPiece);
                if (oldPieceNBT.hasKey("CustomArmor")) {
                    AdvancedArmor.instance.getHandler().getBoostService().clearCache();
                    Armor armor = AdvancedArmor.instance.getHandler().getArmorFromString(NBTUtils.getArmorName(oldPiece));
                    if (AdvancedArmor.instance.getConfigManager().getShards().contains("Extra.unequipped")) {
                        player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Extra.unequipped").replace("%armor_type_piece%", getArmorType(oldPiece.getType())).replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", getArmorType(oldPiece.getType()))));
                    }
                }
            }
        }

        if (newPiece == null || newPiece.getType() == Material.AIR) {
            return;
        }
        NBTItem newPieceNBT = new NBTItem(newPiece);
        if (!newPieceNBT.hasKey("CustomArmor")) {
            return;
        }
        if (newPieceNBT.hasKey("CustomArmor")) {
            AdvancedArmor.instance.getHandler().getBoostService().clearCache();
            Armor armor = AdvancedArmor.instance.getHandler().getArmorFromString(NBTUtils.getArmorName(newPiece));
            if (AdvancedArmor.instance.getConfigManager().getShards().contains("Extra.equipped")) {
                player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Extra.equipped").replace("%armor_type_piece%", getArmorType(newPiece.getType())).replace("%armor_type_display%", armor.getDisplayName()).replace("%armor_type%", getArmorType(newPiece.getType()))));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void blockSwapping(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        if (item == null || event.getHand() == null) {
            return;
        }

        final EquipmentSlot armorItemSlot = item.getType().getEquipmentSlot();
        if (armorItemSlot == EquipmentSlot.HAND || armorItemSlot == EquipmentSlot.OFF_HAND || item.getType().isBlock()) {
            return;
        }
        ItemStack equippedArmor = null;

        // Check which armor piece is already equipped
        if (armorItemSlot == EquipmentSlot.HEAD) {
            equippedArmor = player.getInventory().getHelmet();
        } else if (armorItemSlot == EquipmentSlot.CHEST) {
            equippedArmor = player.getInventory().getChestplate();
        } else if (armorItemSlot == EquipmentSlot.LEGS) {
            equippedArmor = player.getInventory().getLeggings();
        } else if (armorItemSlot == EquipmentSlot.FEET) {
            equippedArmor = player.getInventory().getBoots();
        }

        // If two pieces of armor are equal, the client will do nothing.
        if (item != null && item.getType() != Material.AIR && equippedArmor != null  && equippedArmor.getType() != Material.AIR) {
            if (equippedArmor.getType().toString().contains("_HELMET") && item.getType().toString().contains("_HELMET")){
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);
            }else  if (equippedArmor.getType().toString().contains("_CHESTPLATE") && item.getType().toString().contains("_CHESTPLATE")){
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);
            }else  if (equippedArmor.getType().toString().contains("_LEGGINGS") && item.getType().toString().contains("_LEGGINGS")){
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);
            }else  if (equippedArmor.getType().toString().contains("_BOOTS") && item.getType().toString().contains("_BOOTS")){
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player)
            return;
        if (event.getEntity().getKiller() == null || !(event.getEntity().getKiller() instanceof org.bukkit.entity.Player))
            return;
        if (!this.entityShardMap.containsKey(event.getEntityType()))
            return;
        getShardFromMob(event.getEntityType()).forEach((shard, chance) -> {
            double random = ThreadLocalRandom.current().nextDouble(0, 100); // Generate a random number between 0 and 100
        if (random <= chance) {
                NBTItem nbtItem = new NBTItem(ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Shards." + shard.getShardTypeName() + ".shard-item")).build(), true);
                nbtItem.setBoolean("ArmorShard", Boolean.TRUE);
                event.getEntity().getKiller().getInventory().addItem(nbtItem.getItem());
            if (AdvancedArmor.instance.getConfigManager().getShards().contains("Shards." + shard.getShardTypeName() + ".message")){
                event.getEntity().getKiller().sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Shards." + shard.getShardTypeName() + ".message")));
            }
            }
        });
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (!this.blockShardMap.containsKey(event.getBlock().getType()))
            return;
        Player player = event.getPlayer();

        getShardFromMaterial(event.getBlock().getType()).forEach((shard, chance) -> {
            double random = ThreadLocalRandom.current().nextDouble(0, 100); // Generate a random number between 0 and 100
        if (random <= chance) {
                NBTItem nbtItem = new NBTItem(ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Shards." + shard.getShardTypeName() + ".shard-item")).build(), true);
                nbtItem.setBoolean("ArmorShard", Boolean.TRUE);
                player.getInventory().addItem(nbtItem.getItem());
            if (AdvancedArmor.instance.getConfigManager().getShards().contains("Shards." + shard.getShardTypeName() + ".message")){
                player.sendMessage(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Shards." + shard.getShardTypeName() + ".message")));
            }
            }
        });
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        boolean found = false;
        for (String armor : AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shop.command-names")){
            if (event.getMessage().equalsIgnoreCase("/" + armor)){
                found = true;
                break;
            }
        }
        if (found) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            ShopUI shopUI = new ShopUI();
            player.openInventory(shopUI.getInventory());
        }
    }


}
