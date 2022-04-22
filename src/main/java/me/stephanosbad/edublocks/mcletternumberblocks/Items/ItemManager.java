package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
import io.th0rgal.oraxen.compatibilities.provided.itembridge.OraxenItemBridge;
import io.th0rgal.oraxen.items.OraxenItems;
import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import me.stephanosbad.edublocks.mcletternumberblocks.WordDict;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ItemManager extends CompatibilityProvider<McLetterNumberBlocks> implements Listener {

    private final List<Material> list = Arrays.asList(
            Material.ACACIA_LOG,
            Material.SPRUCE_LOG,
            Material.OAK_LOG,
            Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG,
            Material.BIRCH_LOG);

    public static ItemStack getBlocks(String commandString) {
        return OraxenItems.getItemById(commandString).build();
    }

    public static Set<String> getCharacterBlockNames() {
        var retValue = new HashSet<String>();

        for (var letter : LetterFactors.values()) {
            retValue.add(letter.id);
        }
        return retValue;
    }

    @EventHandler
    public void onBreakWoodOrBlock(BlockBreakEvent e) {
        //e.getPlayer().sendMessage("Blam! " + e.getBlock().getBlockData().getMaterial().name());
        //System.out.println("Blam! " + e.getBlock().getBlockData().getMaterial().name());
        var player = e.getPlayer();
        player.getInventory().getItemInMainHand();
        player.getInventory().getItemInMainHand().getEnchantments();
        if (!(player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))) {
            //If there is no silk touch on it
            if (
                    list.contains(e.getBlock().getBlockData().getMaterial()) &&
                            Math.random() < 0.05) {
                woodBlockBreak(e);
            }
 /*           else
            {
                onStickWhack(e);
            }*/
        }
    }

    private void woodBlockBreak(BlockBreakEvent e) {
        var block = LetterFactors.randomPickOraxenBlock();
        var player = e.getPlayer();
        if (block != null) {

            player.getWorld().dropItemNaturally(e.getBlock().getLocation(), block);

        }
    }
/*
    public void onStickWhack(BlockBreakEvent e)
    {
        var hand = e.getPlayer().getInventory().getItemInMainHand();

        if(!hand.getType().name().toLowerCase(Locale.ROOT).contains("gold")) {
                Bukkit.getLogger().info("Not hit with gold: " + hand.getType().name());
                return;
        }


        var testBlock = e.getBlock();
        var c = testForLetter(testBlock);
        if(c == '\0')
        {
            Bukkit.getLogger().info("Not a letter block: " + testBlock.getDrops());
            return;
        }
        var lateralDirection = checkLateralBlocks(testBlock);
        StringBuilder outString = new StringBuilder();
        if(lateralDirection.isValid())
        {
            while((c = testForLetter(testBlock) )!= '\0')
            {
                outString.append(c);
                testBlock = offsetBlock(testBlock, lateralDirection);
            }
        }
        Bukkit.getLogger().info(outString.toString());
        if(WordDict.Words.contains(outString.toString()))
        {
            Bukkit.getLogger().info("Hit");
        }
        else
        {
            Bukkit.getLogger().info("Miss");
        }
    }

    private Block offsetBlock(Block testBlock, LateralDirection lateralDirection)
    {
        var x = testBlock.getX() + lateralDirection.xOffset;
        var y = testBlock.getY();
        var z = testBlock.getZ() + lateralDirection.zOffset;
        return testBlock.getWorld().getBlockAt(x, y, z);
    }

    private LateralDirection checkLateralBlocks(Block testBlock)
    {
        var retValue = new LateralDirection(0, 0) ;
        var world = testBlock.getWorld();
        var x = testBlock.getX();
        var y = testBlock.getY();
        var z = testBlock.getZ();
        boolean xUp = testForLetter( world.getBlockAt(x + 1, y, z)) != '\0';
        boolean xDown = testForLetter( world.getBlockAt(x - 1, y, z)) != '\0';
        boolean zUp = testForLetter( world.getBlockAt(x, y, z + 1)) != '\0';
        boolean zDown = testForLetter( world.getBlockAt(x, y, z - 1)) != '\0';

        if(xUp && !xDown && !zUp && !zDown)
        {
            retValue.xOffset = 1;
        }
        else if (!xUp && xDown && !zUp && !zDown)
        {
            retValue.xOffset = -1;
        }
        else if (!xUp && !xDown && zUp && !zDown)
        {
            retValue.zOffset = 1;
        }
        else if (!xUp && !xDown && !zUp && zDown)
        {
            retValue.zOffset = -1;
        }

        return retValue;

    }

*/
    char testForLetter(Block testBlock)
    {
        AtomicReference<String> match = new AtomicReference<>("");
        if(Arrays.stream(LetterFactors.values()).anyMatch((v) ->
        {

            if(testBlock.getType() == Material.NOTE_BLOCK && testBlock.getDrops().stream().anyMatch((w) ->
            {
                Bukkit.getLogger().info(OraxenItems.getIdByItem(w) + " : " + v.id);
                return Objects.equals(OraxenItems.getIdByItem(w), v.id);
            }))
            {
                match.set(v.id);
                return true;
            }
            else return false;
        }))
        {
            return match.get().toLowerCase(Locale.ROOT).toCharArray()[0];
        }
        return '\0';
    }
}

