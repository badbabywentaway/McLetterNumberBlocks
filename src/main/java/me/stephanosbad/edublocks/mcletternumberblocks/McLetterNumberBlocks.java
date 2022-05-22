package me.stephanosbad.edublocks.mcletternumberblocks;

import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.compatibilities.CompatibilitiesManager;
import io.th0rgal.oraxen.items.OraxenItems;
import io.th0rgal.oraxen.shaded.kyori.adventure.text.Component;
import me.stephanosbad.edublocks.mcletternumberblocks.Commands.CharBlock;
import me.stephanosbad.edublocks.mcletternumberblocks.Config.ConfigDataHandler;
import me.stephanosbad.edublocks.mcletternumberblocks.Items.ItemManager;
import me.stephanosbad.edublocks.mcletternumberblocks.Utility.FileUtils;
import me.stephanosbad.edublocks.mcletternumberblocks.Utility.WordDict;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.List.of;
import static org.bukkit.Bukkit.getPluginManager;


public final class McLetterNumberBlocks extends JavaPlugin {

    /**
     * Economy plugin
     */
    public Economy econ = null;

    /**
     * Is Vault economy available
     */
    public boolean vaultEconomyEnabled = false;

    /**
     * Is Oraxen loaded (mandatory)
     */
    public boolean oraxenLoaded = false;

    /**
     * Oraxen plugin
     */
    public static Plugin oraxenPlugin;

    /**
     * Location of configuration data handler
     */
    public ConfigDataHandler configDataHandler;

    /**
     * Primary enable
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

        if ((oraxenPlugin =  OraxenPlugin.get()) != null) {
            oraxenLoaded = true;
            CompatibilitiesManager.addCompatibility("McLetterNumberBlocks", ItemManager.class);
            var oraxenFolder =  OraxenPlugin.get().getDataFolder();
            try {
                FileUtils.copyResourcesRecursively(Objects.requireNonNull(this.getClass().getResource("/Oraxen")), oraxenFolder);
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }

        if (setupEconomy()) {
            vaultEconomyEnabled = setupEconomy();
        }
        System.out.println("Vault " + (vaultEconomyEnabled ? "confirmed." : "not available."));

        if(getCommand(CharBlock.CommandName) != null) {
            getCommand(CharBlock.CommandName).setExecutor(new CharBlock());
            getCommand(CharBlock.CommandName).setTabCompleter(new CharBlock());
        }
        getPluginManager().registerEvents(new ItemManager(this), this);
    }

    /**
     * Disabler
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Minecraft Letter/Number Block Plugin Stopping");
    }

    /**
     * Set up economy plugin (Vault alone as of now)
     * @return Successfulness
     */
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

    public static List<File> getRecursive(File directory) {

        List<File> retValue = List.of();
        var faFiles = directory.listFiles();
        for (File file : faFiles) {
            if (file.getName().matches("^(.*?)")) {
                retValue.add(file);
            }
            if (file.isDirectory()) {
                retValue.addAll(getRecursive(file));
            }

        }
        return retValue;
    }
}

