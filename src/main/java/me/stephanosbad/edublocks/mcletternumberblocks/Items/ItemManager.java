package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
import io.th0rgal.oraxen.items.OraxenItems;
import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
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
    public void onBreakWood(BlockBreakEvent e) {
        var player = e.getPlayer();
        player.getInventory().getItemInMainHand();
        player.getInventory().getItemInMainHand().getEnchantments();
        if (!(player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))) {
            //If there is no silk touch on it
            if (
                    list.contains(e.getBlock().getBlockData().getMaterial()) &&
                            Math.random() < 0.1) {
                var block = LetterFactors.randomPickOraxenBlock();
                if (block != null) {

                    player.getWorld().dropItemNaturally(e.getBlock().getLocation(), block);

                }
            }
        }
    }

    @EventHandler
    public void onStickWhack(BlockDamageEvent e)
    {
        if(!Objects.requireNonNull(e.getPlayer().getInventory().getItemInMainHand().getItemMeta()).getDisplayName().contains("gold"))
        {
            return;
        }

        var testBlock = e.getBlock();
        var c = testForLetter(testBlock);
        if(c == '\0')
        {
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


    char testForLetter(Block testBlock)
    {
        AtomicReference<String> match = new AtomicReference<>("");
        if(Arrays.stream(LetterFactors.values()).anyMatch((v) ->
        {

            if(Objects.equals(v.id, testBlock.getBlockData().getMaterial().name()))
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

