package me.stephanosbad.edublocks.mcletternumberblocks.Rewards;

import org.bukkit.entity.Player;

public abstract class CurrencyReward extends Reward {

    /**
     * @param minimumRewardCount
     * @param multiplier
     * @param minimumThreshold
     * @param maximumRewardCap
     */
    CurrencyReward(double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super(minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
    }

    abstract void applyReward(Player player, double score);

}
