package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract class Reward {

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
