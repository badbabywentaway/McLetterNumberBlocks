package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.th0rgal.oraxen.compatibilities.CompatibilityProvider;
import io.th0rgal.oraxen.items.OraxenItems;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanicFactory;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.stephanosbad.edublocks.mcletternumberblocks.utility.*;
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
import org.jetbrains.annotations.NotNull;

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
    private LocationPair exclude = null;
    private LocationPair include = null;
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

    public List<Reward> rewards = new ArrayList<>();

    /**
     * @param localPlugin
     */
    public ItemManager(McLetterNumberBlocks localPlugin) {
        this.plugin = localPlugin;
        try {
            worldGuardPlugin = WorldGuardPlugin.inst();
            worldGuard = WorldGuard.getInstance();
            if (worldGuardPlugin != null && worldGuard != null) {
                Bukkit.getLogger().info("WorldGuard found.");
            } else {
                throw new NullPointerException("Class variable did not instantiate");
            }
        } catch (Exception | Error e) {
            Bukkit.getLogger().info("WorldGuard not available.");
        }

        try {
            griefPrevention = GriefPrevention.instance;
            if (griefPrevention != null) {
                Bukkit.getLogger().info("GriefPrevention found.");
            } else {
                throw new NullPointerException("Class variable did not instantiate");
            }
        } catch (Exception | Error e) {
            Bukkit.getLogger().info("GriefPrevention not available.");
        }

        try {
            setRewards();
        } catch (Exception | Error e) {
            Bukkit.getLogger().info("Rewards not available.");
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
     * combined action for wood block or letter block rewards
     *
     * @param e - block break event
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
            } else {
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
        if (protectedSpot(player, e.getBlock().getLocation(), e.getBlock())) {
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
    public void letterBlockBreak(BlockBreakEvent e) {
        var hand = e.getPlayer().getInventory().getItemInMainHand();

        if (protectedSpot(e.getPlayer(), e.getBlock().getLocation(), e.getBlock())) {
            Bukkit.getLogger().info("Protected block: " + e.getBlock().getLocation());
            return;
        }
        if (!hand.getType().name().toLowerCase(Locale.ROOT).contains("gold")) {
            //Bukkit.getLogger().info("Not hit with gold: " + hand.getType().name());
            return;
        }

        var testBlock = e.getBlock();
        var score = 0D;
        var c = testForLetter(e.getPlayer(), testBlock);
        if (c.first == '\0') {
            //Bukkit.getLogger().info("Not a letter block: " + testBlock.getDrops());
            return;
        }
        var lateralDirection = checkLateralBlocks(e.getPlayer(), testBlock);
        StringBuilder outString = new StringBuilder();
        List<Location> blockArray = new ArrayList<>(List.of());

        if (lateralDirection.isValid()) {
            while (c.first != '\0') {
                score += c.second + 10;
                blockArray.add(testBlock.getLocation());
                outString.append(c.first);
                testBlock = offsetBlock(testBlock, lateralDirection);
                c = testForLetter(e.getPlayer(), testBlock);
            }
        }
        //Bukkit.getLogger().info(outString + " in dictionary of " + WordDict.singleton.Words.size());
        if (WordDict.singleton.Words.contains(outString.toString().toLowerCase(Locale.ROOT))) {
            //e.setDropItems(false);
            e.setCancelled(true);
            e.getPlayer().sendMessage("Hit: " + score);

            for (var locationOfBlock : blockArray) {
                e.getBlock().getWorld().getBlockAt(locationOfBlock).setType(Material.AIR);
            }
            applyScore(e.getPlayer(), score);
        } else {
            e.getPlayer().sendMessage("Miss");
        }
    }

    private void applyScore(Player player, double score) {
        player.sendMessage(rewards.size() + " reward types");
        for (var reward : rewards) {
            if (reward instanceof VaultCurrencyReward) {
                ((VaultCurrencyReward) reward).applyReward(player, score);
            } else if (reward instanceof DropReward) {
                ((DropReward) reward).applyReward(player.getLocation(), score);
            }
        }
    }

    /**
     * @param testBlock
     * @param lateralDirection
     * @return
     */
    private Block offsetBlock(Block testBlock, LateralDirection lateralDirection) {
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
    private @NotNull LateralDirection checkLateralBlocks(Player player, @NotNull Block testBlock) {
        var retValue = new LateralDirection(0, 0);
        var world = testBlock.getWorld();
        var x = testBlock.getX();
        var y = testBlock.getY();
        var z = testBlock.getZ();

        boolean xUp = testForLetter(player, world.getBlockAt(x + 1, y, z)).first != '\0';
        boolean xDown = testForLetter(player, world.getBlockAt(x - 1, y, z)).first != '\0';
        boolean zUp = testForLetter(player, world.getBlockAt(x, y, z + 1)).first != '\0';
        boolean zDown = testForLetter(player, world.getBlockAt(x, y, z - 1)).first != '\0';

        if (xUp && !xDown && !zUp && !zDown) {
            retValue.xOffset = 1;
        } else if (!xUp && xDown && !zUp && !zDown) {
            retValue.xOffset = -1;
        } else if (!xUp && !xDown && zUp && !zDown) {
            retValue.zOffset = 1;
        } else if (!xUp && !xDown && !zUp && zDown) {
            retValue.zOffset = -1;
        }

        return retValue;
    }

    /**
     * @param player
     * @param testBlock
     * @return
     */
    SimpleTuple<Character, Double> testForLetter(Player player, Block testBlock) {
        if (protectedSpot(player, testBlock.getLocation(), testBlock)) {
            Bukkit.getLogger().info("Part of word is protected: " + testBlock.getLocation());
            return new SimpleTuple('\0', 0);
        }
        if (!(testBlock.getState().getBlockData() instanceof NoteBlock)) {
            return new SimpleTuple('\0', 0);
        }
        AtomicReference<SimpleTuple<Character, Double>> match = new AtomicReference<>(new SimpleTuple<>('\0', 0D));
        var variation = getCustomVariation(testBlock);
        if (Arrays.stream(LetterFactors.values()).anyMatch((v) -> {
            boolean found = variation == v.customVariation;
            if (found) {
                match.set(new SimpleTuple<>(v.character, v.frequencyFactor));

            }
            return found;
        })) {
            return match.get();
        }
        return new SimpleTuple('\0', 0);
    }

    /**
     * @param block
     * @return
     */
    int getCustomVariation(Block block) {
        NoteBlock noteBlock = (NoteBlock) block.getState().getBlockData();
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
    boolean protectedSpot(Player player, Location location, Block block) {
        if (griefPrevention != null && griefPrevention.allowBreak(player, block, location) != null) {
            return true;
        }

        if (worldGuardPlugin != null &&
                !worldGuardPlugin.createProtectionQuery().testBlockBreak(player, block)) {
            return true;
        }

        return ourConfigProtects(location);
    }

    /**
     * @param location
     * @return
     */
    private boolean ourConfigProtects(Location location) {

        if (exclude == null) {
            exclude = new LocationPair(
                    plugin.configDataHandler.configuration.getLocation("exclude/from", null),
                    plugin.configDataHandler.configuration.getLocation("exclude/to", null));
        }

        if (include == null) {
            include = new LocationPair(
                    plugin.configDataHandler.configuration.getLocation("include/from", null),
                    plugin.configDataHandler.configuration.getLocation("include/to", null));
        }

        if (exclude.isValid() && exclude.check(location)) {
            return true;
        }

        return include.isValid() && !include.check(location);
    }

    private void setRewards() {
        for (var t : RewardType.values()) {
            switch (t) {
                case VaultCurrency: {
                    Class<VaultCurrencyReward> clazz = null;
                    var reward = plugin.configDataHandler.configuration.getObject(t.toString(), clazz);
                    if (reward != null) {
                        rewards.add(reward);
                    }
                }
                break;
                case Drop: {
                    Class<List<DropReward>> clazz = null;
                    var rewardStack = plugin.configDataHandler.configuration.getObject(t.toString(), clazz);
                    if (rewardStack != null) {
                        for (var reward : rewardStack) {
                            reward.setMaterial();
                        }
                        rewards.addAll(rewardStack);
                    }
                }
                break;
                default:
                    break;
            }
        }
    }
}
