package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VaultCurrencyReward extends CurrencyReward {
    private McLetterNumberBlocks plugin;

    public VaultCurrencyReward( double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super(minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
    }

    public void setPlugin(McLetterNumberBlocks plugin)
    {
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
/*
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap();
        data.put("minimumRewardCount", minimumRewardCount);
        data.put("multiplier", multiplier);
        data.put("minimumThreshold", minimumThreshold);
        data.put("maximumRewardCap", maximumRewardCap);
        return data;
    }

    public static VaultCurrencyReward deserialize(HashMap<String,Object> data){

        double minimumRewardCount = (data.get("minimumRewardCount") instanceof Double)? (double)data.get("minimumRewardCount")  :null;
        double multiplier = (data.get("multiplier") instanceof Double)? (double)data.get("multiplier")  :null;
        double minimumThreshold = (data.get("minimumThreshold") instanceof Double)? (double)data.get("minimumThreshold")  :null;
        double maximumRewardCap = (data.get("maximumRewardCap") instanceof Double)? (double)data.get("maximumRewardCap")  :null;

        return new VaultCurrencyReward(minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
    }*/
}
