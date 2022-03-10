package me.stephanosbad.edublocks.mcletternumberblocks.Items;

//import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.*;


public class ItemManager {


    public static HashMap<String, ItemStack> characterBlocks = new HashMap<>();

    private static final List<Material> anyPlankChoice = new ArrayList<>(Arrays.asList(
            Material.ACACIA_PLANKS,
            Material.BIRCH_PLANKS,
            Material.CRIMSON_PLANKS,
            Material.OAK_PLANKS,
            Material.DARK_OAK_PLANKS,
            Material.JUNGLE_PLANKS,
            Material.SPRUCE_PLANKS,
            Material.WARPED_PLANKS));

    public static void setupBlocks() {
        /*for (char i = 'a'; i <= 'z'; i++) {
            addBlock(i, "L");
        }*/
        for (char i = 'A'; i <= 'Z'; i++) {
            addBlock(i, "U");
        }
        for (char i = '0'; i <= '9'; i++) {
            addBlock(i, "N");
        }
    }

    public static void addBlock(char c, String preChar) {
        for (Material plank : anyPlankChoice) {
            ItemStack item = new ItemStack(plank, 1);
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setLocalizedName(String.format("%s%c_%s", preChar, Character.toUpperCase(c), plank.name()));
            itemMeta.setDisplayName(String.format("%c %s", c, plank.name()));
            itemMeta.setLore(new ArrayList<>(Collections.singletonList(String.format("CharacterValue '%c'", c))));
            if(itemMeta.hasCustomModelData()) {
                int data = itemMeta.getCustomModelData();

            }
            item.setItemMeta(itemMeta);
            System.out.println(itemMeta.getLocalizedName());
            characterBlocks.put(itemMeta.getLocalizedName(), item);
        }
    }

    public static ItemStack getBlocks(String commandString)
    {
        return characterBlocks.getOrDefault(commandString, null);
    }

    public static Set<String> getCharacterBlockNames ()
    {
        return characterBlocks.keySet();
    }

    public static ItemStack getBlockAsItem(int code)
    {
        return characterBlocks.get("ua_block").clone();
    }
}
