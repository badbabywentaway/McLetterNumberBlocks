package me.stephanosbad.edublocks.mcletternumberblocks.Config;

import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;
import me.stephanosbad.edublocks.mcletternumberblocks.Utility.LocationPair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigDataHandler {

    /**
     *
     */
    public YamlConfiguration configuration = null;
    /**
     *
     */
    private File file = null;
    /**
     *
     */
    private final McLetterNumberBlocks plugin;

    /**
     *
     */
    static public String CONFIG_FILE_NAME = "config.yml";

    /**
     * @param plugin
     */
    public ConfigDataHandler(McLetterNumberBlocks plugin)
    {
        this.plugin = plugin;
    }

    /**
     * @throws IOException
     */
    public void loadConfig() throws IOException {
        if(file == null) {
            file = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);
            Bukkit.getLogger().info("File: " + file.getCanonicalPath());
        }


        configuration = YamlConfiguration.loadConfiguration(file);


        InputStream defaultStream = plugin.getResource(CONFIG_FILE_NAME);
        createBlank();
        configuration = YamlConfiguration.loadConfiguration(file);
        /*if(defaultStream != null) {
            configuration.setDefaults( YamlConfiguration.loadConfiguration( new InputStreamReader(defaultStream)));
        }
        else{
            //createBlank();
            //configuration = YamlConfiguration.loadConfiguration(file);
            //defaultStream = plugin.getResource(CONFIG_FILE_NAME);
            //configuration.setDefaults( YamlConfiguration.loadConfiguration( new InputStreamReader(defaultStream)));
            //throw (new IOException("Unable to extract config file " + file.getAbsolutePath() +". Blank recreated"));
        }*/
    }

    /**
     * @throws IOException
     */
    private void createBlank() throws IOException {
        if(file.exists()){
            file.delete();
        }
        if(!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        writeToYaml();
    }
    public void writeToYaml() throws IOException {
        var loc = SampleLocationPair();
        configuration.set("exclude.from", loc.first);
        configuration.set("exclude.to", loc.second);

        var vaultConfiguration = configuration.createSection("VaultCurrency");
        vaultConfiguration.set( "minimumRewardCount", 0.0);
        vaultConfiguration.set( "multiplier", 0.5);
        vaultConfiguration.set( "minimumThreshold", 0.0);
        vaultConfiguration.set( "maximumRewardCap", 2000.0);
        configuration.save(file);
        var dropReward = new HashMap<String, Object>();
        dropReward.put("materialName", Material.IRON_INGOT.toString());
        dropReward.put("minimumRewardCount", 1.0);
        dropReward.put( "multiplier", 0.01);
        dropReward.put( "minimumThreshold", 100.0);
        dropReward.put( "maximumRewardCap", 20.0);
        var dropReward1 = new HashMap<String, Object>();
        dropReward1.put("materialName", Material.GOLD_INGOT.toString());
        dropReward1.put("minimumRewardCount", 0.0);
        dropReward1.put( "multiplier", 0.01);
        dropReward1.put( "minimumThreshold", 500.0);
        dropReward1.put( "maximumRewardCap", 50.0);
        List<Map<String, Object>> dropsConfiguration = List.of(dropReward,dropReward1);
        configuration.set("Drop", dropsConfiguration);
        configuration.save(file);
     }
    public LocationPair SampleLocationPair()
    {
        return new LocationPair (
            new Location(plugin.getServer().getWorld( "world"), -10, 0, -10 ),
            new Location(plugin.getServer().getWorld( "world"), 10, 0, 10 )
        );
    }
}
