package me.stephanosbad.edublocks.mcletternumberblocks;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        file.createNewFile();
    }
}
