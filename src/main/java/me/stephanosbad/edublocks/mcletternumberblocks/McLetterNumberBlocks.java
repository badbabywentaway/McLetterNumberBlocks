package me.stephanosbad.edublocks.mcletternumberblocks;

import io.th0rgal.oraxen.compatibilities.CompatibilitiesManager;
import io.th0rgal.oraxen.items.OraxenItems;
import me.stephanosbad.edublocks.mcletternumberblocks.Items.ItemManager;
//import me.stephanosbad.edublocks.mcletternumberblocks.Items.GetItemCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getPluginManager;

public final class McLetterNumberBlocks extends JavaPlugin {


    private boolean oraxenLoaded = false;
    public static Plugin oraxenPlugin;


    @Override
    public void onEnable() {

        // Plugin startup logic
        System.out.println("Minecraft Letter/Number Block Plugin Starting");

        //Objects.requireNonNull(getCommand("charblock")).setExecutor(new GetItemCommand());
        //Objects.requireNonNull(getCommand("charblock")).setTabCompleter(new GetItemCommand());



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

