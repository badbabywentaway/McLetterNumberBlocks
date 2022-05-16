package me.stephanosbad.edublocks.mcletternumberblocks.Rewards;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Drop reward type.
 */
public class DropReward extends Reward {

    /**
     * Constructor
     * @param materialName - Name of MC material to drop for rewards.
     * @param minimumRewardCount - Minimum number of rewards to drop.
     * @param multiplier - Multiply factor (by score)
     * @param minimumThreshold - Minimum score to apply reward
     * @param maximumRewardCap - Maximum number of rewards of this type.
     */
    public DropReward(String materialName, double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super( minimumRewardCount, multiplier,  minimumThreshold,  maximumRewardCap);
        this.materialName = materialName;
        setMaterial();
    }

    /**
     * Set material based on name
     */
    private void setMaterial() {
        material = Material.valueOf(materialName);
    }

    /**
     * MC material to drop for rewards.
     */
    private Material material;

    /**
     * Name of MC material to drop for rewards.
     */
    public String materialName;

    /**
     * Apply the reward. Drops are location specific.
     * @param location - location to drop the reward.
     * @param score - score in which to apply reward.
     */
    public void applyReward(Location location, double score) {
        double netAmount = (score >= minimumThreshold)
                ? (score - minimumThreshold) * multiplier + minimumRewardCount
                : minimumRewardCount;
        if (netAmount > maximumRewardCap) {
            netAmount = maximumRewardCap;
        }
        int count = (int) Math.round(netAmount);
        if (location.getWorld() == null) {
            return;
        }
        location.getWorld().dropItemNaturally(location, new ItemStack(material, count));
    }
}
