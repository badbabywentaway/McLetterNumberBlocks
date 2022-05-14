package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DropReward extends Reward {

    public DropReward(String materialName, double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super( minimumRewardCount, multiplier,  minimumThreshold,  maximumRewardCap);
        this.materialName = materialName;
        setMaterial();
    }

    public void setMaterial() {
        material = Material.valueOf(materialName);
    }

    public Material material;
    public String materialName;

    public void applyReward(Location location, double score) {
        Bukkit.getLogger().info("Drops for " + score);
        if (score < minimumThreshold) {
            return;
        }
        double netAmount = (score - minimumThreshold) * multiplier + minimumRewardCount;

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
