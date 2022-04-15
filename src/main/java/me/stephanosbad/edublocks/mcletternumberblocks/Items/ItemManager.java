package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
import io.th0rgal.oraxen.items.OraxenItems;
import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
