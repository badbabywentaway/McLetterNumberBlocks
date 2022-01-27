package me.stephanosbad.edublocks.mcletternumberblocks;

import org.bukkit.plugin.java.JavaPlugin;

public final class McLetterNumberBlocks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Minecraft Letter/Number Block Plugin Starting");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Minecraft Letter/Number Block Plugin Stopping");
    }
}
