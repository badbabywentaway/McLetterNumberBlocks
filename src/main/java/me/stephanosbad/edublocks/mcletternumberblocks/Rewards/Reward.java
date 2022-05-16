package me.stephanosbad.edublocks.mcletternumberblocks.Rewards;

/**
 * Reward to apply for scoring a word. Super class.
 */
public abstract class Reward {

    /**
     * Constructor
     * @param minimumRewardCount - Minimum number of rewards to drop.
     * @param multiplier - Multiply factor (by score)
     * @param minimumThreshold - Minimum score to apply reward
     * @param maximumRewardCap - Maximum number of rewards of this type.
     */
    Reward(double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap)
    {
        this.minimumRewardCount = minimumRewardCount;
        this.multiplier = multiplier;
        this.minimumThreshold = minimumThreshold;
        this.maximumRewardCap = maximumRewardCap;
    }

    public double minimumRewardCount;
    public double multiplier;
    public double minimumThreshold;
    public double maximumRewardCap;
}
