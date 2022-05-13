package me.stephanosbad.edublocks.mcletternumberblocks;

import io.th0rgal.oraxen.compatibilities.CompatibilitiesManager;
import me.stephanosbad.edublocks.mcletternumberblocks.Items.ItemManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.bukkit.Bukkit.getPluginManager;


public final class McLetterNumberBlocks extends JavaPlugin {

    private Economy econ = null;
    private Permission perms = null;
    private boolean vaultEconomyEnabled = false;
    /**
     *
     */
    public boolean oraxenLoaded = false;

    /**
     *
     */
    public static Plugin oraxenPlugin;

    /**
     *
     */
    public ConfigDataHandler configDataHandler;

    /**
     *
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Minecraft Letter/Number Block Plugin Starting");
        configDataHandler = new ConfigDataHandler(this);
        try {
            configDataHandler.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            WordDict.init(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ((oraxenPlugin = getServer().getPluginManager().getPlugin("oraxen")) != null) {
            oraxenLoaded = true;
            CompatibilitiesManager.addCompatibility("McLetterNumberBlocks", ItemManager.class);
        }

        if (setupEconomy() )
        {
            vaultEconomyEnabled = setupEconomy();
        }
        System.out.println("Vault " + (vaultEconomyEnabled ? "confirmed" : "not available"));
        getPluginManager().registerEvents(new ItemManager(this), this);
    }

    /**
     *
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Minecraft Letter/Number Block Plugin Stopping");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}

