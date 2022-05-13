package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class VaultCurrencyReward extends CurrencyReward {
    double minimumRewardCount;
    McLetterNumberBlocks plugin;

    VaultCurrencyReward(McLetterNumberBlocks plugin, int minimumRewardCount, double minimumThreshold, double maximumAmountCap, double multiplier) {
        this.plugin = plugin;
        this.multiplier = multiplier;
        this.minimumRewardCount = minimumRewardCount;
        this.minimumThreshold = minimumThreshold;
        this.maximumAmountCap = maximumAmountCap;
    }


    @Override
    void applyReward(Player player, double score) {

        if (score < minimumThreshold) {
            return;
        }

        double netAmount = (score - minimumThreshold) * multiplier + minimumRewardCount;

        if (netAmount > maximumAmountCap) {
            netAmount = maximumAmountCap;
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
