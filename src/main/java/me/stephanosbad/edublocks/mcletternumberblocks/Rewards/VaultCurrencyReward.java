package me.stephanosbad.edublocks.mcletternumberblocks.Rewards;

import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

/**
 *
 */
public class VaultCurrencyReward extends CurrencyReward {
    /**
     *
     */
    private McLetterNumberBlocks plugin;

    /**
     * @param minimumRewardCount
     * @param multiplier
     * @param minimumThreshold
     * @param maximumRewardCap
     */
    public VaultCurrencyReward( double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super(minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
    }

    /**
     * @param plugin
     */
    public void setPlugin(McLetterNumberBlocks plugin)
    {
        this.plugin = plugin;
    }

    /**
     * @param player
     * @param score
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
