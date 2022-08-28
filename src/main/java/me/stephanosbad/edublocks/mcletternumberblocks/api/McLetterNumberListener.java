package me.stephanosbad.edublocks.mcletternumberblocks.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class McLetterNumberListener implements Listener {

    private final int maxCount = 100;
    private List<WordCompleteEvent> WordCompleteEventStack = new ArrayList<>();
    private List<NumberHitEvent> numberHitEventStack = new ArrayList<>();

    public WordCompleteEvent popWordCompleteEvent() {
        return WordCompleteEventStack.size() > 0 ? WordCompleteEventStack.remove(0) : null;
    }

    public NumberHitEvent popNumberHitEvent() {
        return numberHitEventStack.size() > 0 ? numberHitEventStack.remove(0) : null;
    }

    @EventHandler
    public void onWordCompleteEvent(WordCompleteEvent event) {

        if(WordCompleteEventStack.size() < maxCount) {
            WordCompleteEventStack.add(event);
        }
    }

    @EventHandler
    public void onNumberHitEvent(NumberHitEvent event) {
        if(numberHitEventStack.size() < maxCount) {
            numberHitEventStack.add(event);
        }
    }
}
