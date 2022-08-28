

// Require the player to drop a certain number of a certain type of item.
package me.stephanosbad.edublocks.mcletternumberblocksquestplugin;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;

import me.stephanosbad.edublocks.mcletternumberblocks.api.NumberHitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class NumberObjective extends CustomObjective {
    // Get the Quests plugin
    Quests qp = (Quests)Bukkit.getServer().getPluginManager().getPlugin("Quests");

    // Construct the objective
    public NumberObjective() {
        this.setName("Minecraft Number Block Objective");
        this.setAuthor("Jane Doe");
        this.setItem("ANVIL", (short)0);
        //this.setShowCount(true);
        this.addStringPrompt("Result","Enter the result of the formula that the player must enter:", "");
        this.setDisplay("Drop %Formula%: %Result%");
        this.addStringPrompt("Formula", "Enter the formula player must solve:", "");
    }

    // Catch the Bukkit event for a player dropping an item
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
            /*
            // Check if the item the player dropped is the one user specified
            if (evt.getItemDrop().getItemStack().getType().equals(type)) {
                // Add to the objective's progress, completing it if requirements were met
                incrementObjective(evt.getPlayer(), this, stack.getAmount(), quest);
            }*/
        }
    }
}
