package me.stephanosbad.edublocks.mcletternumberblocks;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;

/**
 *
 */
public final class WordDict {

    /**
     *
     */
    public static HashSet<String> Words = new HashSet<>();

    /**
     * @throws FileNotFoundException
     */
    public static void init() throws FileNotFoundException {
        Gson gson = new Gson();
        gson.fromJson(new FileReader("Words.json"), WordDict.class);
    }
}
