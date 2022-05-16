package me.stephanosbad.edublocks.mcletternumberblocks.Rewards;

import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

/**
 * Currency reward for Vault plugin
 */
public class VaultCurrencyReward extends CurrencyReward {

    /**
     * Reference to root plugin
     */
    private McLetterNumberBlocks plugin;

    /**
     * Constructor
     * @param plugin - root plugin.
     * @param minimumRewardCount - Minimum number of rewards to drop.
     * @param multiplier - Multiply factor (by score)
     * @param minimumThreshold - Minimum score to apply reward
     * @param maximumRewardCap - Maximum number of rewards of this type.
     */
    public VaultCurrencyReward(McLetterNumberBlocks plugin, double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super(minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
        this.plugin = plugin;
    }

    /**
     * Need to manually set root plugin, needed by this reward type, to maintain serialization.
     * @param plugin - root plugin
     */
    public void setPlugin(McLetterNumberBlocks plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Apply the vault currency.
     * @param player - Player to apply
     * @param score - Score to apply.
     */
    @Override
    public void applyReward(Player player, double score) {

        player.sendMessage("Currency Applied for score " + score );
        double netAmount = (score >= minimumThreshold)
                ? (score - minimumThreshold) * multiplier + minimumRewardCount
                : minimumRewardCount;
        if (netAmount > maximumRewardCap) {
            netAmount = maximumRewardCap;
        }
        double count = Math.round(netAmount);
        if (plugin.vaultEconomyEnabled) {
            EconomyResponse r = plugin.econ.depositPlayer(player, count);
            if (!r.transactionSuccess()) {
                System.out.println(r.errorMessage);
            }
        }
    }
}
