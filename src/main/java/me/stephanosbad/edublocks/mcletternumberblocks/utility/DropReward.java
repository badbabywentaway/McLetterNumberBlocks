package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DropReward extends Reward {

    DropReward(Material material, int minimumRewardCount, double minimumThreshold, double maximumAmountCap, double multiplier) {
        this.material = material;
        this.multiplier = multiplier;
        this.minimumRewardCount = minimumRewardCount;
        this.minimumThreshold = minimumThreshold;
        this.maximumAmountCap = maximumAmountCap;
    }

    Material material = Material.AIR;
    int minimumRewardCount = 0;

    void applyReward(Location location, double score) {
        if (score < minimumThreshold) {
            return;
        }
        double netAmount = (score - minimumThreshold) * multiplier + minimumRewardCount;

        if (netAmount > maximumAmountCap) {
            netAmount = maximumAmountCap;
        }
        int count = (int) Math.round(netAmount);
        if (location.getWorld() == null) {
            return;
        }
        location.getWorld().dropItemNaturally(location, new ItemStack(material, count));
    }
}
