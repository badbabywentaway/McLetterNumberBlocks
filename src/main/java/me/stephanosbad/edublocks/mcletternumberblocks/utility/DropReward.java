package me.stephanosbad.edublocks.mcletternumberblocks.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DropReward extends Reward {

    public DropReward(String materialName, double minimumRewardCount, double multiplier, double minimumThreshold, double maximumRewardCap) {
        super( minimumRewardCount, multiplier,  minimumThreshold,  maximumRewardCap);
        this.materialName = materialName;
        setMaterial();
    }

    public void setMaterial() {
        material = Material.valueOf(materialName);
    }

    private Material material;
    public String materialName;

    public void applyReward(Location location, double score) {
        Bukkit.getLogger().info("Drops for " + score);
        if (score < minimumThreshold) {
            return;
        }
        double netAmount = (score - minimumThreshold) * multiplier + minimumRewardCount;

        if (netAmount > maximumRewardCap) {
            netAmount = maximumRewardCap;
        }
        int count = (int) Math.round(netAmount);
        if (location.getWorld() == null) {
            return;
        }
        location.getWorld().dropItemNaturally(location, new ItemStack(material, count));
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
        data.put("materialName", materialName);
        return data;
    }

    public static DropReward deserialize(HashMap<String,Object> data){

        String materialName = (data.get("materialName") instanceof String)? (String)data.get("materialName")  :null;
        double minimumRewardCount = (data.get("minimumRewardCount") instanceof Double)? (double)data.get("minimumRewardCount")  :null;
        double multiplier = (data.get("multiplier") instanceof Double)? (double)data.get("multiplier")  :null;
        double minimumThreshold = (data.get("minimumThreshold") instanceof Double)? (double)data.get("minimumThreshold")  :null;
        double maximumRewardCap = (data.get("maximumRewardCap") instanceof Double)? (double)data.get("maximumRewardCap")  :null;

        var clazz = new DropReward(materialName, minimumRewardCount, multiplier, minimumThreshold, maximumRewardCap);
        clazz.setMaterial();
        return clazz;
    }*/
}
