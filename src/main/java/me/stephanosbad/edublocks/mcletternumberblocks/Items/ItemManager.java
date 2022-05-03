package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
import io.th0rgal.oraxen.items.OraxenItems;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanicFactory;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.stephanosbad.edublocks.mcletternumberblocks.utility.LocationPair;
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

/**
 *
 */
public class ItemManager extends CompatibilityProvider<McLetterNumberBlocks> implements Listener {

    /**
     *
     */
    private final List<Material> list = Arrays.asList(
            Material.ACACIA_LOG,
            Material.SPRUCE_LOG,
            Material.OAK_LOG,
            Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG,
            Material.BIRCH_LOG);

    /**
     *
     */
    WorldGuard worldGuard = null;

    /**
     *
     */
    WorldGuardPlugin worldGuardPlugin = null;

    /**
     *
     */
    GriefPrevention griefPrevention = null;

    /**
     * @param localPlugin
     */
    public ItemManager(McLetterNumberBlocks localPlugin)
    {
        this.plugin = localPlugin;
        try {
            worldGuardPlugin = WorldGuardPlugin.inst();
            worldGuard = WorldGuard.getInstance();
            if(worldGuardPlugin != null && worldGuard != null) {
                Bukkit.getLogger().info("WorldGuard found.");
            }
            else
            {
                throw new NullPointerException("Class variable did not instantiate");
            }
        }
        catch(Exception | Error e)
        {
            Bukkit.getLogger().info("WorldGuard not available.");
        }

        try {
            griefPrevention = GriefPrevention.instance;
            if(griefPrevention != null) {
                Bukkit.getLogger().info("GriefPrevention found.");
            }
            else
            {
                throw new NullPointerException("Class variable did not instantiate");
            }
        }
        catch(Exception | Error e)
        {
            Bukkit.getLogger().info("GriefPrevention not available.");
        }

    }

    /**
     * @param commandString
     * @return
     */
    public static ItemStack getBlocks(String commandString) {
        return OraxenItems.getItemById(commandString).build();
    }

    /**
     * @return
     */
    public static Set<String> getCharacterBlockNames() {
        var retValue = new HashSet<String>();

        for (var letter : LetterFactors.values()) {
            retValue.add(letter.id);
        }
        return retValue;
    }

    /**
     * @param e
     */
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

    /**
     * @param e
     */
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

    /**
     * @param e
     */
    public void letterBlockBreak(BlockBreakEvent e)
    {
        var hand = e.getPlayer().getInventory().getItemInMainHand();

        if(protectedSpot(e.getPlayer(), e.getBlock().getLocation(), e.getBlock())) {
            Bukkit.getLogger().info("Protected block: " + e.getBlock().getLocation());
            return;
        }
        if(!hand.getType().name().toLowerCase(Locale.ROOT).contains("gold")) {
                //Bukkit.getLogger().info("Not hit with gold: " + hand.getType().name());
                return;
        }

        var testBlock = e.getBlock();
        var c = testForLetter(e.getPlayer(), testBlock);
        if(c == '\0')
        {
            //Bukkit.getLogger().info("Not a letter block: " + testBlock.getDrops());
            return;
        }
        var lateralDirection = checkLateralBlocks(e.getPlayer(), testBlock);
        StringBuilder outString = new StringBuilder();
        if(lateralDirection.isValid())
        {
            while(c != '\0')
            {
                outString.append(c);
                testBlock = offsetBlock(testBlock, lateralDirection);
                c = testForLetter(e.getPlayer(), testBlock);
            }
        }
        Bukkit.getLogger().info(outString.toString());
        if(WordDict.Words.contains(outString.toString().toLowerCase(Locale.ROOT)))
        {
            Bukkit.getLogger().info("Hit");
        }
        else
        {
            Bukkit.getLogger().info("Miss");
        }
    }

    /**
     * @param testBlock
     * @param lateralDirection
     * @return
     */
    private Block offsetBlock(Block testBlock, LateralDirection lateralDirection)
    {
        var x = testBlock.getX() + lateralDirection.xOffset;
        var y = testBlock.getY();
        var z = testBlock.getZ() + lateralDirection.zOffset;
        return testBlock.getWorld().getBlockAt(x, y, z);
    }

    /**
     * @param player
     * @param testBlock
     * @return
     */
    private LateralDirection checkLateralBlocks(Player player, Block testBlock)
    {
        var retValue = new LateralDirection(0, 0) ;
        var world = testBlock.getWorld();
        var x = testBlock.getX();
        var y = testBlock.getY();
        var z = testBlock.getZ();


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

    /**
     * @param player
     * @param testBlock
     * @return
     */
    char testForLetter(Player player, Block testBlock)
    {
        if(protectedSpot(player, testBlock.getLocation(), testBlock))
        {
            Bukkit.getLogger().info("Part of word is protected: " + testBlock.getLocation());
            return '\0';
        }
        if(!(testBlock.getState().getBlockData() instanceof NoteBlock))
        {
            //Bukkit.getLogger().info("Block is not a noteblock");
            return '\0';
        }
        AtomicReference<Character> match = new AtomicReference<>('\0');
        var variation = getCustomVariation(testBlock);
        //Bukkit.getLogger().info("Variation: " + variation);
        if(Arrays.stream(LetterFactors.values()).anyMatch((v) -> {
            boolean found = variation == v.customVariation;
            if(found)
            {
                match.set(v.character);
            }
            return found;
        }))
        {
            //Bukkit.getLogger().info("Matched: " + match);
            return match.get();
        }
        return '\0';
    }

    /**
     * @param block
     * @return
     */
    int getCustomVariation(Block block) {
        //Bukkit.getLogger().info("Block is noteblock: " + (block.getState().getBlockData() instanceof NoteBlock));
        NoteBlock noteBlock  = (NoteBlock) block.getState().getBlockData();
        NoteBlockMechanic mech = NoteBlockMechanicFactory.getBlockMechanic((int) (noteBlock
                .getInstrument().getType()) * 25 + (int) noteBlock.getNote().getId()
                + (noteBlock.isPowered() ? 400 : 0) - 26);
        return mech.getCustomVariation();
    }

    /**
     * @param player
     * @param location
     * @param block
     * @return
     */
    boolean protectedSpot(Player player, Location location, Block block)
    {
        if(griefPrevention != null && griefPrevention.allowBreak(player, block, location) != null){
            return true;
        }

        if(worldGuardPlugin != null &&
                !worldGuardPlugin.createProtectionQuery().testBlockBreak(player, block ))
        {
            return true;
        }

        return ourConfigProtects(location);
    }

    /**
     * @param location
     * @return
     */
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

        return include.isValid() && !include.check(location);

    }
}
