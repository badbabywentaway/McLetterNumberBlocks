package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import org.bukkit.entity.Player;

public abstract class CurrencyReward extends Reward {

    abstract void applyReward(Player player, double score);

}
