package me.stephanosbad.edublocks.mcletternumberblocksquestsmodule;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import me.stephanosbad.edublocks.mcletternumberblocks.api.WordCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.Objects;

public class WordObjective  extends CustomObjective {

    Quests qp = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

    // Construct the objective
    public WordObjective() {
        this.setName("Minecraft Number Block Objective");
        this.setAuthor("StephanosBad");
        this.setItem("ANVIL", (short)0);
        //this.setShowCount(true);
        this.addStringPrompt("Riddle","Enter the riddle to solve:", "");
        this.addStringPrompt("Answer", "Enter answer player must spell:", "");
        this.setDisplay("Solve %Formula%");
    }

    @EventHandler
    public void onWordCompleteEvent(WordCompleteEvent evt)
    {
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
            if (Objects.equals(
                    evt.blockWord,
                    this.getDataForPlayer(
                            evt.getPlayer(),
                            this,
                            quest).get("Answer").toString())) {
                incrementObjective(evt.getPlayer(), this, 1, quest);
            }
        }
    }
}
