package me.stephanosbad.edublocks.mcletternumberblocks.Rewards;

public abstract class Reward {

    /**
     * @param minimumRewardCount
     * @param multiplier
     * @param minimumThreshold
     * @param maximumRewardCap
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
