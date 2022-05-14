package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import org.bukkit.entity.Player;

public abstract class CurrencyReward extends Reward {

    CurrencyReward(double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super(minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
    }

    abstract void applyReward(Player player, double score);

}
