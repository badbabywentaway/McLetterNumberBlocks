package me.stephanosbad.edublocks.mcletternumberblocks.Rewards;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 */
public class DropReward extends Reward {

    /**
     * @param materialName
     * @param minimumRewardCount
     * @param multiplier
     * @param minimumThreshold
     * @param maximumRewardCap
     */
    public DropReward(String materialName, double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super( minimumRewardCount, multiplier,  minimumThreshold,  maximumRewardCap);
        this.materialName = materialName;
        setMaterial();
    }

    /**
     *
     */
    public void setMaterial() {
        material = Material.valueOf(materialName);
    }

    /**
     *
     */
    private Material material;

    /**
     *
     */
    public String materialName;

    /**
     * @param location
     * @param score
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
