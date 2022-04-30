package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
import io.th0rgal.oraxen.items.OraxenItems;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanicFactory;
import io.th0rgal.protectionlib.compatibilities.WorldGuardCompat;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import me.stephanosbad.edublocks.mcletternumberblocks.WordDict;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ItemManager extends CompatibilityProvider<McLetterNumberBlocks> implements Listener {
    McLetterNumberBlocks localPlugin;
    private final List<Material> list = Arrays.asList(
            Material.ACACIA_LOG,
            Material.SPRUCE_LOG,
            Material.OAK_LOG,
            Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG,
            Material.BIRCH_LOG);
    WorldGuardCompat worldGuard;
    public ItemManager(McLetterNumberBlocks localPlugin)
    {
        this.localPlugin = localPlugin;
        worldGuard = new WorldGuardCompat(localPlugin, localPlugin);
    }

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
    public void onBreakWoodOrLetter(BlockBreakEvent e) {
        //e.getPlayer().sendMessage("Blam! " + e.getBlock().getBlockData().getMaterial().name());
        //System.out.println("Blam! " + e.getBlock().getBlockData().getMaterial().name());
        var player = e.getPlayer();
        player.getInventory().getItemInMainHand();
        player.getInventory().getItemInMainHand().getEnchantments();

        // TBD: leave if this block is grief prevented.

        if (!(player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))) {
            //If there is no silk touch on it
            if (list.contains(e.getBlock().getBlockData().getMaterial()) && Math.random() < 0.05) {
                woodBlockBreak(e);
            }
            else {
                letterBlockBreak(e);
            }
        }
    }

    private void woodBlockBreak(BlockBreakEvent e) {
        var block = LetterFactors.randomPickOraxenBlock();
        var player = e.getPlayer();
        if(protectedSpot(player, e.getBlock().getLocation(), e.getBlock()))
        {
            Bukkit.getLogger().info("Cannot drop letter blocks in protected area: " + e.getBlock().getLocation());
            return;
        }
        if (block != null) {

            player.getWorld().dropItemNaturally(e.getBlock().getLocation(), block);

        }
    }

    public void letterBlockBreak(BlockBreakEvent e)
    {
        var hand = e.getPlayer().getInventory().getItemInMainHand();

        if(protectedSpot(e.getPlayer(), e.getBlock().getLocation(), e.getBlock())) {
            Bukkit.getLogger().info("Protected block: " + e.getBlock().getLocation());
            return;
        }
        if(!hand.getType().name().toLowerCase(Locale.ROOT).contains("gold")) {
                Bukkit.getLogger().info("Not hit with gold: " + hand.getType().name());
                return;
        }

        var testBlock = e.getBlock();
        var c = testForLetter(e.getPlayer(), testBlock);
        if(c == '\0')
        {
            Bukkit.getLogger().info("Not a letter block: " + testBlock.getDrops());
            return;
        }
        var lateralDirection = checkLateralBlocks(e.getPlayer(), testBlock);
        StringBuilder outString = new StringBuilder();
        if(lateralDirection.isValid())
        {
            while((c = testForLetter(e.getPlayer(), testBlock) )!= '\0')
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

    private LateralDirection checkLateralBlocks(Player player, Block testBlock)
    {
        var retValue = new LateralDirection(0, 0) ;
        var world = testBlock.getWorld();
        var x = testBlock.getX();
        var y = testBlock.getY();
        var z = testBlock.getZ();

        //TBD: is this xyz protected by grief prevention?

        boolean xUp = testForLetter( player, world.getBlockAt(x + 1, y, z)) != '\0';
        boolean xDown = testForLetter( player, world.getBlockAt(x - 1, y, z)) != '\0';
        boolean zUp = testForLetter( player, world.getBlockAt(x, y, z + 1)) != '\0';
        boolean zDown = testForLetter( player, world.getBlockAt(x, y, z - 1)) != '\0';

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

    char testForLetter(Player player, Block testBlock)
    {
        if(protectedSpot(player, testBlock.getLocation(), testBlock))
        {
            Bukkit.getLogger().info("Part of word is protected: " + testBlock.getLocation());
            return '\0';
        }
        AtomicReference<String> match = new AtomicReference<>("");
        var variation = getCustomVariation(testBlock);
        if(Arrays.stream(LetterFactors.values()).anyMatch((v) -> variation == v.customVariation))
        {
            return match.get().toLowerCase(Locale.ROOT).toCharArray()[0];
        }
        return '\0';
    }

    int getCustomVariation(Block block) {
        NoteBlock noteBlock  = (NoteBlock) block.getState().getBlockData();
        NoteBlockMechanic mech = NoteBlockMechanicFactory.getBlockMechanic((int) (noteBlock
                .getInstrument().getType()) * 25 + (int) noteBlock.getNote().getId()
                + (noteBlock.isPowered() ? 400 : 0) - 26);
        return mech.getCustomVariation();
    }

    boolean protectedSpot(Player player, Location location, Block block)
    {

        GriefPrevention griefPrevention = null;
        try {
            griefPrevention = GriefPrevention.instance;
        }
        catch(Exception e)
        {

            Bukkit.getLogger().info(e.getLocalizedMessage());

        }
        if(griefPrevention != null && griefPrevention.allowBreak(player, block, location) != null){
            return true;
        }

        if(worldGuard != null && !worldGuard.canBreak(player, location))
        {
            return true;
        }

        return ourConfigProtects(location);
    }

    class simpleTuple<T, U>
    {
        public T first;
        public U second;
        simpleTuple(T first, U second)
        {
            this.first = first;
            this.second = second;
        }

    }

    class  simplerTuple<T> extends simpleTuple<T, T>
    {
        simplerTuple(T first, T second) {

            super(first, second);
        }
    }

    class LocationPair extends simplerTuple<Location>
    {
        LocationPair(Location first, Location second)
        {
            super(first, second);
        }

        public boolean isValid() {
            return first != null && second != null &&
            first.getWorld() == second.getWorld();
        }

        public boolean check(Location location) {
            return location.getWorld() == first.getWorld() &&
                    inMcRange(location.getX(), first.getX(), second.getX()) &&
                    inMcRange(location.getZ(), first.getZ(), second.getZ());

        }

        private boolean inMcRange(double x, double x1, double x2) {
            if(x1 > x2)
            {
                return x <= x1 && x >= x2;
            }
            return x >= x1 && x <= x2;
        }
    }

    private boolean ourConfigProtects(Location location)
    {
        var exclude = new LocationPair(
                plugin.configDataHandler.configuration.getLocation("exclude/from", null),
                plugin.configDataHandler.configuration.getLocation("exclude/to", null));

        var include = new LocationPair(
                plugin.configDataHandler.configuration.getLocation("include/from", null),
                plugin.configDataHandler.configuration.getLocation("include/to", null));

        if(exclude.isValid() && exclude.check(location))
        {
            return true;
        }

        if(include.isValid() && !include.check(location))
        {
            return true;
        }

        return false;

    }
}
