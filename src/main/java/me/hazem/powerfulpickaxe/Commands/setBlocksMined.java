package me.hazem.powerfulpickaxe.Commands;

import me.hazem.powerfulpickaxe.BreaksInfo;
import me.hazem.powerfulpickaxe.PowerFulPickaxe;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class setBlocksMined implements CommandExecutor {

    private final PowerFulPickaxe plugin;

    public setBlocksMined(PowerFulPickaxe plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player p) {

            if (!(p.hasPermission("powerfulpickaxe.setblocksmined"))) {
                p.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
                return false;
            }
            if (!(args.length > 0)) {
                p.sendMessage(ChatColor.RED + "Usage: /setBlocksMined <amount>");
                return false;
            }
            int amount;
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "Please enter a valid Number!");
                return false;
            }

            if(amount < 0){
                p.sendMessage(ChatColor.RED + "Please enter a valid Number!");
                return false;
            }
            if (p.getInventory().getItemInMainHand().getItemMeta() == null) {
                p.sendMessage(ChatColor.RED + "Please put your Pickaxe in Your Main Hand!");
                return false;
            }
            if (!(ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Ultimate Pickaxe"))) {
                p.sendMessage(ChatColor.RED + "Please put your Pickaxe in Your Main Hand!");
                return false;
            }


            NamespacedKey BlocksMinedKey = new NamespacedKey(plugin, "BlocksMined");
            NamespacedKey MiningTierKey = new NamespacedKey(plugin, "MiningTier");
            ItemStack Pickaxe = p.getInventory().getItemInMainHand();
            ItemMeta meta = Pickaxe.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();

            if (!(container.has(BlocksMinedKey, PersistentDataType.INTEGER))) {
                container.set(BlocksMinedKey, PersistentDataType.INTEGER, 0);
            }

            if (!(container.has(MiningTierKey, PersistentDataType.INTEGER))) {
                container.set(MiningTierKey, PersistentDataType.INTEGER, 1);
            }

            int oldTier = container.get(MiningTierKey, PersistentDataType.INTEGER);
            int tier = -1;

            //update BlocksMined
            container.set(BlocksMinedKey, PersistentDataType.INTEGER, amount);

            //update tier
            for (int i = 2; i < 13; i++) {
                if (amount >= 10000000) {
                    container.set(MiningTierKey, PersistentDataType.INTEGER, 11);
                    tier = 11;
                    break;
                }
                if (amount <= BreaksInfo.getMiningInfo().get(i)) {
                    container.set(MiningTierKey, PersistentDataType.INTEGER, (i - 1));
                    tier = i - 1;
                    break;
                }
            }

            if (tier == -1) System.out.println("setBlocksMined error: tier = -1");

            //update lore
            ArrayList<String> lore = new ArrayList<String>();

            lore.add(" ");
            lore.add(ChatColor.GOLD + "Tier " + BreaksInfo.toRoman(tier));
            lore.add(ChatColor.GRAY + "Grants a " + ChatColor.GREEN + BreaksInfo.getDropChance().get(tier) + "%" + ChatColor.GRAY + " chance to get ");
            lore.add(ChatColor.GREEN + "2x" + ChatColor.GRAY + " drops from ores");
            if(tier == 11){
                lore.add(ChatColor.GRAY + "And a " + ChatColor.GREEN + BreaksInfo.getDropChance().get(110) + "%" + ChatColor.GRAY + " chance to get");
                lore.add(ChatColor.GREEN + "x3" + ChatColor.GRAY + " drops from ores");
            }else{
                lore.add(ChatColor.DARK_GRAY + "" + BreaksInfo.getMiningInfo().get(tier + 1) + " blocks to tier up!");
            }
            lore.add(" ");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Counter: " + ChatColor.YELLOW + amount + " Ores");

            meta.setLore(lore);
            Pickaxe.setItemMeta(meta);

            if(oldTier != tier){
                if(oldTier < tier){
                    p.sendMessage(ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(oldTier) + ChatColor.RESET + " on your " + meta.getDisplayName() + ChatColor.RESET + " was upgraded to " + ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(tier));
                }else{
                    p.sendMessage(ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(oldTier) + ChatColor.RESET + " on your " + meta.getDisplayName() + ChatColor.RESET + " was demoted to " + ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(tier));
                }
            }
            p.sendMessage(ChatColor.DARK_GREEN + "Successfully setting BlocksMined on your " + meta.getDisplayName() + " to " + ChatColor.AQUA + amount);

            return true;
        } else {
            System.out.println("Players only can run this command!");
        }

        return false;
    }
}