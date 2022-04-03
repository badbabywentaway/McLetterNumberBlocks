package me.stephanosbad.edublocks.mcletternumberblocks;

import io.th0rgal.oraxen.pack.generation.ResourcePack;
import me.stephanosbad.edublocks.mcletternumberblocks.Items.ItemManager;
import me.stephanosbad.edublocks.mcletternumberblocks.Items.getItemCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class McLetterNumberBlocks extends JavaPlugin {

    private PluginManager pluginManager;
    private Logger logger;

    private static McLetterNumberBlocks singleton;

    public static McLetterNumberBlocks getInstance()
    {
        return singleton;
    }

    @Override
    public void onEnable() {

        singleton = this;

        pluginManager = getServer().getPluginManager();
        logger = getLogger();

        // Plugin startup logic
        System.out.println("Minecraft Letter/Number Block Plugin Starting");
        ItemManager.setupBlocks();
        Objects.requireNonNull(getCommand("charblock")).setExecutor(new getItemCommand());
        Objects.requireNonNull(getCommand("charblock")).setTabCompleter(new getItemCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Minecraft Letter/Number Block Plugin Stopping");
    }
}

