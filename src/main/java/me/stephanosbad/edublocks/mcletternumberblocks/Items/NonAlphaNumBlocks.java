package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.inventory.ItemStack;

/**
 * Non alphanumeric character references
 */
public enum NonAlphaNumBlocks {
    PLUS('+', "plus_block"),
    MINUS('-', "minus_block"),
    MULTIPLY('*', "multiply_block"),
    DIVISION('/', "divide_block");

    public final char charVal;
    public final String oraxenBlockName;
    public ItemStack itemStack;

    /**
     * Constructor
     *
     * @param c         - character
     * @param blockName - block name in which to map
     */
    NonAlphaNumBlocks(char c, String blockName) {
        this.charVal = c;
        this.oraxenBlockName = blockName;
        itemStack = OraxenItems.getItemById(oraxenBlockName).build();
    }
}
