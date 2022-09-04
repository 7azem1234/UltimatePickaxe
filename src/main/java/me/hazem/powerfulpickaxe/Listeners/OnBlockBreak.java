package me.hazem.powerfulpickaxe.Listeners;

import me.hazem.powerfulpickaxe.BreaksInfo;
import me.hazem.powerfulpickaxe.PowerFulPickaxe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class OnBlockBreak implements Listener {

    private final PowerFulPickaxe plugin;

    public OnBlockBreak(PowerFulPickaxe plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null) return;
        if (ChatColor.stripColor(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Ultimate Pickaxe")) {
            boolean IsAllowed = false;

            for (int i = 0; i < BreaksInfo.getOres().size(); i++) {
                if (e.getBlock().getType() == BreaksInfo.getOres().get(i)) {
                    IsAllowed = true;
                    break;
                }
            }

            if (!IsAllowed) return;

            Player p = e.getPlayer();
            ItemStack Pickaxe = p.getInventory().getItemInMainHand();
            ItemMeta meta = Pickaxe.getItemMeta();
            double FortuneLvl = Pickaxe.getEnchantmentLevel(Enchantment.DIG_SPEED);

            NamespacedKey BlocksMinedKey = new NamespacedKey(plugin, "BlocksMined");
            NamespacedKey MiningTierKey = new NamespacedKey(plugin, "MiningTier");

            PersistentDataContainer container = meta.getPersistentDataContainer();
            int BlocksMined;
            int MiningTier;


            if (!(container.has(BlocksMinedKey, PersistentDataType.INTEGER))) {
                container.set(BlocksMinedKey, PersistentDataType.INTEGER, 0);
            }

            if (!(container.has(MiningTierKey, PersistentDataType.INTEGER))) {
                container.set(MiningTierKey, PersistentDataType.INTEGER, 1);
            }


            BlocksMined = container.get(BlocksMinedKey, PersistentDataType.INTEGER);
            MiningTier = container.get(MiningTierKey, PersistentDataType.INTEGER);



            Block Block = e.getBlock();
            int BlocksRequired;
            int NextTierBlocksRequired;
            int NextTierChance;
            if(MiningTier == 11){
                BlocksRequired = -1;
                NextTierBlocksRequired = -1;
                NextTierChance = -1;
            }else{
                BlocksRequired = BreaksInfo.getMiningInfo().get(MiningTier + 1);
                if(MiningTier == 10){
                    NextTierBlocksRequired = -1;
                }else{
                    NextTierBlocksRequired = BreaksInfo.getMiningInfo().get(MiningTier + 2);
                }
                NextTierChance = BreaksInfo.getDropChance().get(MiningTier + 1);
            }

            int TierChance = BreaksInfo.getDropChance().get(MiningTier);


            container.set(BlocksMinedKey, PersistentDataType.INTEGER, BlocksMined + 1);
            int NewCounter = BlocksMined + 1;

            boolean UpgradeTier = false;

            if(NewCounter >= BlocksRequired){
                if(BlocksRequired != -1){
                    container.set(MiningTierKey, PersistentDataType.INTEGER, MiningTier + 1);
                    p.sendMessage(ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(MiningTier) + ChatColor.RESET + " on your " + meta.getDisplayName() + ChatColor.RESET + " was upgraded to " + ChatColor.DARK_PURPLE + "Mining Tier " + BreaksInfo.toRoman(MiningTier + 1));
                    UpgradeTier = true;
                }
            }

            ArrayList<String> lore = new ArrayList<String>();

            lore.add(" ");

            if (UpgradeTier) {
                lore.add(ChatColor.GOLD + "Tier " + BreaksInfo.toRoman(MiningTier + 1));
                lore.add(ChatColor.GRAY + "Grants a " + ChatColor.GREEN + NextTierChance + "%" + ChatColor.GRAY + " chance to get ");
                lore.add(ChatColor.GREEN + "2x" + ChatColor.GRAY + " drops from ores");
                if((MiningTier + 1) == 11){
                    lore.add(ChatColor.GRAY + "And a " + ChatColor.GREEN + BreaksInfo.getDropChance().get(110) + "%" + ChatColor.GRAY + " chance to get");
                    lore.add(ChatColor.GREEN + "x3" + ChatColor.GRAY + " drops from ores");
                }else{
                    lore.add(ChatColor.DARK_GRAY + "" + NextTierBlocksRequired + " blocks to tier up!");
                }
            } else {
                lore.add(ChatColor.GOLD + "Tier " + BreaksInfo.toRoman(MiningTier));
                lore.add(ChatColor.GRAY + "Grants a " + ChatColor.GREEN + TierChance + "%" + ChatColor.GRAY + " chance to get ");
                lore.add(ChatColor.GREEN + "2x" + ChatColor.GRAY + " drops from ores");;
                if(MiningTier == 11){
                    lore.add(ChatColor.GRAY + "And a " + ChatColor.GREEN + BreaksInfo.getDropChance().get(110) + "%" + ChatColor.GRAY + " chance to get");
                    lore.add(ChatColor.GREEN + "x3" + ChatColor.GRAY + " drops from ores");
                }else{
                    lore.add(ChatColor.DARK_GRAY + "" + BlocksRequired + " blocks to tier up!");
                }
            }

            lore.add(" ");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Counter: " + ChatColor.YELLOW + NewCounter + " Ores");
            meta.setLore(lore);
            Pickaxe.setItemMeta(meta);



            int DropChance;

            if (!UpgradeTier) {
                DropChance = BreaksInfo.getDropChance().get(MiningTier);
            } else {
                DropChance = BreaksInfo.getDropChance().get(MiningTier + 1);
            }

            int FinalDropChance;

            double probabilityForDrop = new Random().nextDouble();

            if(MiningTier == 11){

                int TripleDropChance = BreaksInfo.getDropChance().get(110);
                if (probabilityForDrop <= ((double) TripleDropChance / 100.0)) {
                    FinalDropChance = 3;
                }else if (probabilityForDrop <= ((double) DropChance / 100.0)){
                    FinalDropChance = 2;
                }else{
                    FinalDropChance = 1;
                }

            }else{

                if (probabilityForDrop <= ((double) DropChance / 100.0)) {
                    FinalDropChance = 2;
                }else{
                    FinalDropChance = 1;
                }
            }


            Location BlockLocation = Block.getLocation();

            for (ItemStack stack : Block.getDrops(Pickaxe)) {
                stack.setAmount(stack.getAmount() * FinalDropChance);
                Objects.requireNonNull(BlockLocation.getWorld()).dropItem(BlockLocation, stack);
                System.out.println(FinalDropChance);
            }

            e.setDropItems(false);

        }
    }
}

