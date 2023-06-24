package me.stephanosbad.edublocks.mcletternumberblocks.Commands;

import me.stephanosbad.edublocks.mcletternumberblocks.Items.LetterBlock;
import me.stephanosbad.edublocks.mcletternumberblocks.Items.NonAlphaNumBlocks;
import me.stephanosbad.edublocks.mcletternumberblocks.Items.NumericBlock;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Test command only executable from console. Gives a string of blocks to player.
 *      charblock user blocks
 *      user - player near which to drop naturally
 *      blocks - string of block characters to drop naturally near player
 */
public class CharBlock implements CommandExecutor, TabCompleter {


    /**
     * String which to use for registering this command
     */
    public static String CommandName = "charblock";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender != sender.getServer().getConsoleSender())
            return true;

        if (args.length < 2) {
            return true;
        }

        String givePlayerName = args[0];
        Player givePlayer = Bukkit.getPlayerExact(givePlayerName);

        if (givePlayer == null || !givePlayer.isOnline()) {
            return true;
        }

        String characterString = args[1].toLowerCase(Locale.ROOT);

        for (var c : characterString.toCharArray()) {
            ItemStack dropStack = null;
            for (var test : NonAlphaNumBlocks.values()) {
                if (test.charVal == c) {
                    dropStack = test.itemStack;
                }
            }
            if (dropStack == null) {
                var isThere = Arrays.stream(LetterBlock.values()).filter(it -> it.character == c).findFirst();
                if(!isThere.isEmpty())
                {
                    dropStack = isThere.get().itemStack;

                }
                else
                {
                    var isThereNum = Arrays.stream(NumericBlock.values()).filter(it -> it.c == c).findFirst();
                    if(!isThereNum.isEmpty())
                    dropStack = isThereNum.get().itemStack;
                }
            }
            if (givePlayer.getLocation().getWorld() != null) {
                givePlayer.getLocation().getWorld().dropItemNaturally(givePlayer.getLocation(), dropStack);
            }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> completions = new ArrayList<>();
        String mainArg;

        if (args.length == 0) return null;

        if (args.length == 1) {
            mainArg = args[0].toLowerCase();
            List<String> onlinePlayers = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                onlinePlayers.add(p.getName());
            }
            StringUtil.copyPartialMatches(mainArg, onlinePlayers, completions);
        }

        if(args.length == 2) {
            List<String> characterMatches = new ArrayList<>();
            for(var letter: LetterBlock.values())
            {
                characterMatches.add(String.valueOf(letter.character));
            }
            for(var number: NumericBlock.values())
            {
                characterMatches.add(String.valueOf(number.c));
            }
            for(var non : NonAlphaNumBlocks.values())
            {
                characterMatches.add(non.oraxenBlockName.toLowerCase(Locale.ROOT).split("_")[0]);
            }
            completions = characterMatches;

        }


        return completions;
    }
}
