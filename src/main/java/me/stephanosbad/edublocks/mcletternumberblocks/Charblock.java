package me.stephanosbad.edublocks.mcletternumberblocks;

import io.th0rgal.oraxen.items.OraxenItems;
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
import java.util.List;
import java.util.Locale;

public class Charblock  implements CommandExecutor, TabCompleter {

    public static String CommandName = "charblock";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender != sender.getServer().getConsoleSender())
            return true;


        // charblock <user> <string>
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
            var dropStack = OraxenItems.getItemById(c + "_block").build();
            if (givePlayer.getLocation().getWorld() != null) {
                givePlayer.getLocation().getWorld().dropItemNaturally(givePlayer.getLocation(), dropStack);
            }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
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
        return completions;
    }
}
