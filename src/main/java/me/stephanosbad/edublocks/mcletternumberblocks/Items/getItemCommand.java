package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class getItemCommand implements CommandExecutor, TabCompleter {

    // Colorize Strings!
    public static String colorize(String str) {
        /*
         Since the translateAlternateColors method works with only one char and overwrites the previous one,
         we are going to replace '&' with '§' and then run that method with all the '§'s.
         */
        str = str.replace('&', '§');
        ChatColor.translateAlternateColorCodes('§', str);
        return str;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("Console command only.");
            return true;
        }

        // give <user> <type> <item> <amount>
        if (args.length < 3) {
            sender.sendMessage(colorize("&7-----[ &eCHARBLOCK Help &7]-----"));

            return true;
        }

        String givePlayerName = args[0];
        Player givePlayer = Bukkit.getPlayerExact(givePlayerName);

        if (givePlayer == null || !givePlayer.isOnline()) {
            sender.sendMessage(colorize(String.format("Player %s not online.", givePlayerName)));
            return true;
        }

        String amountStr = args[2];
        String itemName = args[1].toUpperCase();
        int amount;

        try {
            amount = Integer.parseInt(amountStr);
        } catch (Exception e) {
            sender.sendMessage(colorize("&red Exception: " + e.getMessage()));
            return true;
        }

        ItemStack itemStack = null;

        try {

            itemStack = ItemManager.getBlocks(itemName);
        } catch (Exception e) {
            sender.sendMessage(colorize("&red Exception: " + e.getMessage()));
            return true;
        }

        if (itemStack != null && itemStack.getItemMeta() != null) {
            itemStack.setAmount(amount);
            givePlayer.getInventory().addItem(itemStack);
            return false;

        }

        sender.sendMessage(colorize("&7-----[ &eCHARBLOCK Help &7]-----"));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String
            label, @NotNull String[] args) {
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
        } else if (args.length == 2) {
            mainArg = args[1].toLowerCase();

            List<String> items = new ArrayList<>();

            for (String s : ItemManager.getCharacterBlockNames()) {
                items.add(s.toLowerCase());
            }


            StringUtil.copyPartialMatches(mainArg, items, completions);
        } else if (args.length == 3) {
            mainArg = args[2].toLowerCase();
            List<String> numbers = new ArrayList<>();
            for (int i = 1; i <= 64; i++) {
                numbers.add(i + "");
            }

            StringUtil.copyPartialMatches(mainArg, numbers, completions);
        }

        return completions;
    }
}



