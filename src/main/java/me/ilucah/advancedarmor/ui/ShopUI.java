package me.ilucah.advancedarmor.ui;

import com.iridium.iridiumcolorapi.*;
import me.ilucah.advancedarmor.*;
import me.ilucah.advancedarmor.armor.*;
import me.ilucah.advancedarmor.listener.*;
import me.ilucah.advancedarmor.utilities.*;
import me.ilucah.advancedarmor.utilities.xutils.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ShopUI implements InventoryHolder {
    @NotNull

    public int helmetSlot;

    public int chestplateSlot;

    public int leggingsSlot;

    public int bootsSlot;

    Inventory inventory;
    public String armorType = null;
    public ShopUI(){

        this.inventory = Bukkit.createInventory(this,AdvancedArmor.instance.getConfigManager().getShards().getInt("Menu.size"), IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Menu.name")));
        this.helmetSlot = AdvancedArmor.instance.getConfigManager().getShards().getInt("Menu.helmet-slot");
        this.chestplateSlot = AdvancedArmor.instance.getConfigManager().getShards().getInt("Menu.chestplate-slot");
        this.leggingsSlot = AdvancedArmor.instance.getConfigManager().getShards().getInt("Menu.leggings-slot");
        this.bootsSlot = AdvancedArmor.instance.getConfigManager().getShards().getInt("Menu.boots-slot");
        init();
    }

    public void init(){

if (AdvancedArmor.instance.getConfigManager().getShards().getBoolean("Menu.filler.enabled")){
    ItemStack filler = new ItemBuilder(XMaterial.matchXMaterial(AdvancedArmor.instance.getConfigManager().getShards().getString("Menu.filler.material")).get().parseMaterial()).setName("&7").build();
    int i;
    for (i = 0; i < this.inventory.getSize(); i++) {
        this.inventory.setItem(i, filler);
    }
    }


        for (int loop = 0; loop < ShardListener.getPurchasableArmor().size(); loop++) {
            try {
                ShardObj shard = ShardListener.getShardFromArmorName(((Armor) ShardListener.getPurchasableArmor().get(loop)).getName());
                ItemStack item = ((Armor) ShardListener.getPurchasableArmor().get(loop)).getHelmet().clone();
                ItemMeta meta = item.getItemMeta();
                meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                meta.setDisplayName(IridiumColorAPI.process(AdvancedArmor.instance.getConfigManager().getShards().getString("Shards." + shard.getShardTypeName() + ".click-to-select.name")));
                List<String> lore = AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards." + shard.getShardTypeName() + ".click-to-select.lore");
                lore = IridiumColorAPI.process(lore);
                meta.setLore(lore);
                item.setItemMeta(meta);
                this.inventory.setItem(shard.getMenuSlot(), item);
            } catch (NullPointerException exc) {
            }
        }
            this.inventory.setItem(this.helmetSlot, ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Menu.not-selected-item")).build());
            this.inventory.setItem(this.chestplateSlot, ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Menu.not-selected-item")).build());
            this.inventory.setItem(this.leggingsSlot, ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Menu.not-selected-item")).build());
            this.inventory.setItem(this.bootsSlot, ItemBuilder.fromConfig(AdvancedArmor.instance.getConfigManager().getShards().getConfigurationSection("Menu.not-selected-item")).build());


            if (armorType != null){
                Armor armor = AdvancedArmor.instance.getHandler().getArmorFromString(armorType);
                ShardObj shard = ShardListener.getShardFromArmorName(armor.getName());
                String armorName = armorType;
                ItemStack helmet = armor.getHelmet().clone();
                ItemMeta helmetMeta = helmet.getItemMeta();
                List<String> helmetLore = helmetMeta.hasLore() ? helmetMeta.getLore() : new ArrayList<>();
                helmetLore.add(" ");
                AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards."+ shard.getShardTypeName() +".piece-addon-lore").forEach(l -> helmetLore.add(l.replace("%cost%", String.valueOf(ShardListener.getShardFromArmorName(armorName).getHelmetPrice()))));
                helmetMeta.setLore(IridiumColorAPI.process(helmetLore));
                helmet.setItemMeta(helmetMeta);
                this.inventory.setItem(helmetSlot, helmet);

                // Chestplate
                ItemStack chestplate = armor.getChestplate().clone();
                ItemMeta chestplateItemMeta = chestplate.getItemMeta();
                List<String> chestplateLore = chestplateItemMeta.hasLore() ? chestplateItemMeta.getLore() : new ArrayList<>();
                chestplateLore.add(" ");
                AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards."+shard.getShardTypeName() +".piece-addon-lore").forEach(l -> chestplateLore.add(l.replace("%cost%", String.valueOf(ShardListener.getShardFromArmorName(armorName).getChestplatePrice()))));
                chestplateItemMeta.setLore(IridiumColorAPI.process(chestplateLore));
                chestplate.setItemMeta(chestplateItemMeta);
                this.inventory.setItem(chestplateSlot, chestplate);

// Leggings
                ItemStack leggings = armor.getLeggings().clone();
                ItemMeta leggingsItemMeta = leggings.getItemMeta();
                List<String> leggingsLore = leggingsItemMeta.hasLore() ? leggingsItemMeta.getLore() : new ArrayList<>();
                leggingsLore.add(" ");
                AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards."+shard.getShardTypeName() +".piece-addon-lore").forEach(l -> leggingsLore.add(l.replace("%cost%", String.valueOf(ShardListener.getShardFromArmorName(armorName).getLeggingsPrice()))));
                leggingsItemMeta.setLore(IridiumColorAPI.process(leggingsLore));
                leggings.setItemMeta(leggingsItemMeta);
                this.inventory.setItem(leggingsSlot, leggings);

// Boots
                ItemStack boots = armor.getBoots().clone();
                ItemMeta bootMeta = boots.getItemMeta();
                List<String> bootLore = bootMeta.hasLore() ? bootMeta.getLore() : new ArrayList<>();
                bootLore.add(" ");
                AdvancedArmor.instance.getConfigManager().getShards().getStringList("Shards."+shard.getShardTypeName() +".piece-addon-lore").forEach(l -> bootLore.add(l.replace("%cost%", String.valueOf(ShardListener.getShardFromArmorName(armorName).getBootsPrice()))));
                bootMeta.setLore(IridiumColorAPI.process(bootLore));
                boots.setItemMeta(bootMeta);
                this.inventory.setItem(bootsSlot, boots);

        }

}

    public void setArmorType(String armorType) {
        this.armorType = armorType;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
