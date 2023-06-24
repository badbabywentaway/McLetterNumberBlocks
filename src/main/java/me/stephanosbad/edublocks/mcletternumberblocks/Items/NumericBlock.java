package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.inventory.ItemStack;

public enum NumericBlock {
    BLOCK_0('0'),
    BLOCK_1('1'),
    BLOCK_2('2'),
    BLOCK_3('3'),
    BLOCK_4('4'),
    BLOCK_5('5'),
    BLOCK_6('6'),
    BLOCK_7('7'),
    BLOCK_8('8'),
    BLOCK_9('9'),
    ;

    public final char c;
    public final ItemStack itemStack;

    NumericBlock(char c) {
        this.c = c;
        this.itemStack = OraxenItems.getItemById(c + "_block").build();
    }
}
