package me.stephanosbad.edublocks.mcletternumberblocks;

import io.th0rgal.oraxen.compatibilities.CompatibilitiesManager;
import me.stephanosbad.edublocks.mcletternumberblocks.Items.ItemManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getPluginManager;

public final class McLetterNumberBlocks extends JavaPlugin {


    public boolean oraxenLoaded = false;
    public static Plugin oraxenPlugin;


    @Override
    public void onEnable() {

        // Plugin startup logic
        System.out.println("Minecraft Letter/Number Block Plugin Starting");


        if ((oraxenPlugin = getServer().getPluginManager().getPlugin("oraxen")) != null) {
            oraxenLoaded = true;
            CompatibilitiesManager.addCompatibility("McLetterNumberBlocks", ItemManager.class);
        }
        getPluginManager().registerEvents(new ItemManager(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Minecraft Letter/Number Block Plugin Stopping");
    }
}

