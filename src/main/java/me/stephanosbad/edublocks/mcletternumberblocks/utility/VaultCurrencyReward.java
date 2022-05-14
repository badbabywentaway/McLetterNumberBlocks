package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class VaultCurrencyReward extends CurrencyReward {
    private McLetterNumberBlocks plugin;

    public VaultCurrencyReward(McLetterNumberBlocks plugin, double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super(minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
        this.plugin = plugin;
    }

    @Override
    public void applyReward(Player player, double score) {

        player.sendMessage("Currency Applied for score " + score );
        if (score < minimumThreshold) {
            return;
        }

        double netAmount = (score - minimumThreshold) * multiplier + minimumRewardCount;

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
