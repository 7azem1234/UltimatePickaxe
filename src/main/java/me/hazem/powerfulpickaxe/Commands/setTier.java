package me.hazem.powerfulpickaxe.Commands;

import me.hazem.powerfulpickaxe.BreaksInfo;
import me.hazem.powerfulpickaxe.PowerFulPickaxe;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
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

public class setTier implements CommandExecutor {

    private final PowerFulPickaxe plugin;

    public setTier(PowerFulPickaxe plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (!(p.hasPermission("powerfulpickaxe.settier"))) {
                p.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
                return false;
            }
            if (!(args.length > 0)) {
                p.sendMessage(ChatColor.RED + "Usage: /setTier <TierNumber>");
                return false;
            }
            int tier = -1;
            if (!(args[0].equals("cancel"))) {
                try {
                    if(args[0].equals("confirm")){
                        tier = Integer.parseInt(args[1]);
                    }else{
                        tier = Integer.parseInt(args[0]);
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.RED + "Please enter a valid number!");
                    return false;
                }
                if (tier < 0) {
                    p.sendMessage(ChatColor.RED + "Please enter a valid Number!");
                    return false;
                }
                if (tier == 0) {
                    p.sendMessage(ChatColor.RED + "You can't set MiningTier to 0!");
                    return false;
                }
                if (tier > 11) {
                    p.sendMessage(ChatColor.RED + "You can't Setting Tier that more than 11");
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
            }

            if (args[0].equals("confirm")) {
                if (args.length < 2) {
                    System.out.println("error: confirming setTier (args.length less then 2)");
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

                int CurrentTier = container.get(MiningTierKey, PersistentDataType.INTEGER);
                int CurrentMinedBlocks = container.get(BlocksMinedKey, PersistentDataType.INTEGER);
                int BlocksRequired;
                boolean sameMinedBlocks = false;
                if (tier == CurrentTier) {
                    BlocksRequired = CurrentMinedBlocks;
                    sameMinedBlocks = true;
                } else {
                    BlocksRequired = BreaksInfo.getMiningInfo().get(tier);
                }


                //PersistentDataContainer
                container.set(MiningTierKey, PersistentDataType.INTEGER, tier);
                container.set(BlocksMinedKey, PersistentDataType.INTEGER, BlocksRequired);

                //editLore
                ArrayList<String> lore = new ArrayList<String>();

                lore.add(" ");
                lore.add(ChatColor.GOLD + "Tier " + BreaksInfo.toRoman(tier));
                lore.add(ChatColor.GRAY + "Grants a " + ChatColor.GREEN + BreaksInfo.getDropChance().get(tier) + "%" + ChatColor.GRAY + " chance to get");
                lore.add(ChatColor.GREEN + "2x" + ChatColor.GRAY + " drops from ores");
                if (tier == 11) {
                    lore.add(ChatColor.GRAY + "And a " + ChatColor.GREEN + BreaksInfo.getDropChance().get(110) + "%" + ChatColor.GRAY + " chance to get");
                    lore.add(ChatColor.GREEN + "x3" + ChatColor.GRAY + " drops from ores");
                } else {
                    lore.add(ChatColor.DARK_GRAY + "" + BreaksInfo.getMiningInfo().get(tier + 1) + " blocks to tier up!");
                }
                lore.add(" ");
                lore.add(" ");
                lore.add(ChatColor.GRAY + "Counter: " + ChatColor.YELLOW + BlocksRequired + " Ores");

                meta.setLore(lore);
                Pickaxe.setItemMeta(meta);

                //sendMessage
                if (!sameMinedBlocks) {
                    if (CurrentTier < tier) {
                        p.sendMessage(ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(CurrentTier) + ChatColor.RESET + " on your " + meta.getDisplayName() + ChatColor.RESET + " was upgraded to " + ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(tier));
                    } else {
                        p.sendMessage(ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(CurrentTier) + ChatColor.RESET + " on your " + meta.getDisplayName() + ChatColor.RESET + " was demoted to " + ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(tier));
                    }
                }
                p.sendMessage(ChatColor.DARK_GREEN + "Successfully setting Mining Tier on your " + meta.getDisplayName() + " to " + ChatColor.AQUA + tier);

                return true;
            } else if (args[0].equals("cancel")) {
                p.sendMessage(ChatColor.RED + "Changing MiningTier was canceled successfully");
                return true;
            } else {


                TextComponent message = new TextComponent("Are you sure?");
                message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                message.setBold(true);
                message.setUnderlined(true);

                TextComponent cancelTask = new TextComponent("[CANCEL]");
                cancelTask.setColor(net.md_5.bungee.api.ChatColor.RED);
                cancelTask.setBold(true);
                cancelTask.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to Cancel Setting MiningTier")));
                cancelTask.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settier cancel"));

                TextComponent extra1 = new TextComponent(" - ");
                extra1.setColor(net.md_5.bungee.api.ChatColor.DARK_GRAY);
                extra1.setBold(true);

                TextComponent ContinueOrCancel = new TextComponent("[CONTINUE]");
                ContinueOrCancel.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                ContinueOrCancel.setBold(true);
                ContinueOrCancel.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to Set MiningTier")));
                ContinueOrCancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settier confirm " + tier));
                ContinueOrCancel.addExtra(extra1);
                ContinueOrCancel.addExtra(cancelTask);

                TextComponent extra = new TextComponent("If you confirm to Set the MiningTier of your Pickaxe, may Counter will change!!!");
                extra.setColor(net.md_5.bungee.api.ChatColor.YELLOW);

                p.spigot().sendMessage(message);
                p.spigot().sendMessage(extra);
                p.spigot().sendMessage(ContinueOrCancel);

                return true;
            }
        } else {
            System.out.println("Players only can run this command!");
        }

        return false;
    }
}
