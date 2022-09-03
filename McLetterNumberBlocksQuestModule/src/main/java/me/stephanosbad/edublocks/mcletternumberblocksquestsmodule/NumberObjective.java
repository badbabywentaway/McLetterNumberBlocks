

// Require the player to drop a certain number of a certain type of item.
package me.stephanosbad.edublocks.mcletternumberblocksquestsmodule;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;

import me.stephanosbad.edublocks.mcletternumberblocks.api.NumberHitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class NumberObjective extends CustomObjective {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");

    Hashtable<Player, String> checkString = new Hashtable<>();
    // Construct the objective
    public NumberObjective() {
        this.setName("Minecraft Number Block Objective");
        this.setAuthor("StephanosBad");
        this.setItem("ANVIL", (short)0);
        //this.setShowCount(true);
        this.addStringPrompt("Result","Enter the result of the formula that the player must enter:", "");
        this.addStringPrompt("Formula", "Enter the formula player must solve:", "");
        this.setDisplay("Solve %Formula%");
    }

    @EventHandler
    public void onNumberHitEvent(NumberHitEvent evt){
        // Make sure to evaluate for all of the player's current quests
        for (Quest quest : qp.getQuester(evt.getPlayer().getUniqueId()).getCurrentQuests().keySet()) {
            Map<String, Object> map = getDataForPlayer(evt.getPlayer(), this, quest);
            if (map == null) {
                continue;
            }

            //ItemStack stack = evt.getItemDrop().getItemStack();
            String userInput = (String) map.get("Item Name");
            EntityType type = EntityType.fromName(userInput);
            // Display error if user-specified item name is invalid
            if (type == null) {
                Bukkit.getLogger().severe("Drop Item Objective has invalid item name: " + userInput);
                continue;
            }

            // Check if the item the player dropped is the one user specified
            if (evt.hitChar == '=') {
                // Add to the objective's progress, completing it if requirements were met
                if(Objects.equals(checkString.getOrDefault(evt.getPlayer(), ""),
                        this.getDataForPlayer(evt.getPlayer(), this, quest).get("Result")) ) {
                    incrementObjective(evt.getPlayer(), this, 1, quest);
                }
                checkString.put(evt.getPlayer(), "");
            }
            else if (Character.isDigit(evt.hitChar))
            {
                checkString.putIfAbsent(evt.getPlayer(), "");
                checkString.put(evt.getPlayer(), checkString.get(evt.getPlayer()) + evt.hitChar);
            }
        }
    }
}
