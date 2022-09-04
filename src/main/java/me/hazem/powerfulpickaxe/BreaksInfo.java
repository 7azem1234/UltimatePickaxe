package me.hazem.powerfulpickaxe;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class BreaksInfo {

    public static HashMap<Integer, Integer> getMiningInfo() {
        return MiningInfo;
    }

    public static HashMap<Integer, Integer> getDropChance() {
        return DropChance;
    }

    public static ArrayList<Material> getOres() {
        return Ores;
    }

    static final HashMap<Integer, Integer> MiningInfo = new HashMap<Integer, Integer>(){{
            put(1, 0);
            put(2, 100);
            put(3, 500);
            put(4, 1500);
            put(5, 5000);
            put(6, 15000);
            put(7, 50000);
            put(8, 150000);
            put(9, 500000);
            put(10, 1000000);
            put(11, 10000000);
        }};

        static final HashMap<Integer, Integer> DropChance = new HashMap<Integer, Integer>(){{
            put(1, 2);
            put(2, 4);
            put(3, 6);
            put(4, 8);
            put(5, 10);
            put(6, 12);
            put(7, 14);
            put(8, 16);
            put(9, 18);
            put(10, 20);
            put(11, 50);
            put(110, 10);
        }};

        static final ArrayList<Material> Ores = new ArrayList<Material>(){{
            add(Material.COBBLESTONE);
            add(Material.COAL_ORE);
            add(Material.COAL_BLOCK);
            add(Material.COPPER_ORE);
            add(Material.COPPER_BLOCK);
            add(Material.IRON_ORE);
            add(Material.IRON_BLOCK);
            add(Material.GOLD_ORE);
            add(Material.GOLD_BLOCK);
            add(Material.REDSTONE_ORE);
            add(Material.REDSTONE_BLOCK);
            add(Material.LAPIS_ORE);
            add(Material.LAPIS_BLOCK);
            add(Material.DIAMOND_ORE);
            add(Material.DIAMOND_BLOCK);
            add(Material.EMERALD_ORE);
            add(Material.EMERALD_BLOCK);
            add(Material.ANCIENT_DEBRIS);
        }};

    final static TreeMap<Integer, String> map = new TreeMap<Integer, String>(){{
        put(1000, "M");
        put(900, "CM");
        put(500, "D");
        put(400, "CD");
        put(100, "C");
        put(90, "XC");
        put(50, "L");
        put(40, "XL");
        put(10, "X");
        put(9, "IX");
        put(5, "V");
        put(4, "IV");
        put(1, "I");
    }};


    public static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }

}
