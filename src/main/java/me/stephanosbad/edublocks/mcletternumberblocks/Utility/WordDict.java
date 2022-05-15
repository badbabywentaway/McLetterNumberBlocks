package me.stephanosbad.edublocks.mcletternumberblocks.Utility;

import com.google.gson.Gson;
import me.stephanosbad.edublocks.mcletternumberblocks.McLetterNumberBlocks;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 *
 */
public final class WordDict {

    /**
     *
     */
    public HashSet<String> Words = new HashSet<>();

    public static WordDict singleton;
    /**
     * @throws FileNotFoundException
     */
    public static void init(McLetterNumberBlocks sourceClass) throws FileNotFoundException {
        Gson gson = new Gson();
        var loader = sourceClass.getClass().getResourceAsStream("/Words.json");
        if(loader==null)
        {
            throw(new FileNotFoundException("Words.json"));
        }
        singleton = gson.fromJson(new InputStreamReader(loader), WordDict.class);

    }
}
