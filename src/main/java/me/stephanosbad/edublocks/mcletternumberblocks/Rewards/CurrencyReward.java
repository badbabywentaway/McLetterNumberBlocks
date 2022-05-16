package me.stephanosbad.edublocks.mcletternumberblocks.Rewards;

import org.bukkit.entity.Player;

/**
 * Super class of currency plugins.
 */
public abstract class CurrencyReward extends Reward {

    /**
     * Constructor
     * @param minimumRewardCount - Minimum number of rewards to drop.
     * @param multiplier - Multiply factor (by score)
     * @param minimumThreshold - Minimum score to apply reward
     * @param maximumRewardCap - Maximum number of rewards of this type.
     */
    CurrencyReward(double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super(minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
    }

    /**
     * Apply the reward.
     * @param player Player to whom to reward.
     * @param score - score to determine reward.
     */
    abstract void applyReward(Player player, double score);

}
