package me.stephanosbad.edublocks.mcletternumberblocks;

import me.stephanosbad.edublocks.mcletternumberblocks.utility.LocationPair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class ConfigDataHandler {


    /**
     *
     */
    public YamlConfiguration configuration = null;
    private File file = null;
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
        if(defaultStream != null) {
            configuration.setDefaults( YamlConfiguration.loadConfiguration( new InputStreamReader(defaultStream)));

        }
        else{
            createBlank();
            throw (new IOException("Unable to extract config file " + file.getAbsolutePath() +". Blank recreated"));
        }

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
        configuration.set("exclude/from", loc.first);
        configuration.set("exclude/to", loc.second);
        configuration.save(file);

    }
    public LocationPair SampleLocationPair()
    {
        return new LocationPair
                (
                        new Location(plugin.getServer().getWorld( "world"), -10, 0, -10 ),
                        new Location(plugin.getServer().getWorld( "world"), 10, 0, 10 )
                );
    }
}

