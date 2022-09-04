package me.hazem.powerfulpickaxe;

import me.hazem.powerfulpickaxe.Commands.setBlocksMined;
import me.hazem.powerfulpickaxe.Commands.setTier;
import me.hazem.powerfulpickaxe.Listeners.OnBlockBreak;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PowerFulPickaxe extends JavaPlugin {


    @Override
    public void onEnable() {

        ItemStack RareNetherite = RareNetheriteRecipe();
        if(RareNetherite == null) return;
        PickaxeRecipe(RareNetherite);

        getServer().getPluginManager().registerEvents(new OnBlockBreak(this), this);
        getCommand("setminedblocks").setExecutor(new setBlocksMined(this));
        getCommand("settier").setExecutor(new setTier(this));

        System.out.println("Plugin starting up...");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private ItemStack RareNetheriteRecipe(){
        ItemStack RareNetherRite = new ItemStack(Material.NETHERITE_INGOT);
        ItemMeta meta = RareNetherRite.getItemMeta();

        if(meta == null) {
            System.out.println("error: RareNetherRite meta is null");
            return null;
        }

        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Rare Netherite");
        meta.setLore(List.of(ChatColor.DARK_PURPLE + "This is a Rare Netherite", ChatColor.DARK_PURPLE + "made by 7azem :)"));

        RareNetherRite.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "RareNetherite");
        ShapedRecipe Netheriterecipe = new ShapedRecipe(key, RareNetherRite);
        Netheriterecipe.shape(
                " X ",
                "XXX",
                " X ");
        Netheriterecipe.setIngredient('X', Material.NETHERITE_INGOT);

        Bukkit.addRecipe(Netheriterecipe);
        return RareNetherRite;
    }

    private void PickaxeRecipe(ItemStack RareNetherRite){
        ItemStack Pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = Pickaxe.getItemMeta();

        if(meta == null) {
            System.out.println("error: Pickaxe meta is null");
            return;
        }
        meta.setDisplayName(ChatColor.GOLD + "Ultimate Pickaxe");

        meta.addEnchant(Enchantment.DIG_SPEED, 10, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 5, true);

        meta.getPersistentDataContainer().set(new NamespacedKey(this, "BlocksMined"), PersistentDataType.INTEGER, 0);
        meta.getPersistentDataContainer().set(new NamespacedKey(this, "MiningTier"), PersistentDataType.INTEGER, 1);

        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" ");
        lore.add(ChatColor.GOLD + "Tier I ");
        lore.add(ChatColor.GRAY + "Grants a " + ChatColor.GREEN + "2%" + ChatColor.GRAY + " chance to get ");
        lore.add(ChatColor.GREEN + "2x" + ChatColor.GRAY + " drops from ores");;
        lore.add(ChatColor.DARK_GRAY + "100 blocks to tier up!");
        lore.add(" ");
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Counter: " + ChatColor.YELLOW + "0 Ores");

        meta.setLore(lore);
        Pickaxe.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "UltimatePickaxe");
        ShapedRecipe Pickaxerecipe = new ShapedRecipe(key, Pickaxe);
        Pickaxerecipe.shape(
                "XXX",
                " K ",
                " K ");
        Pickaxerecipe.setIngredient('X', new RecipeChoice.MaterialChoice.ExactChoice(RareNetherRite));
        Pickaxerecipe.setIngredient('K', Material.STICK);

        Bukkit.addRecipe(Pickaxerecipe);
    }
}

