package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class LetterBlocksEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    public final String blockWord;
    public final Player player;
    public final Location location;
    public final Double score;
    public final Boolean isHit;

    public LetterBlocksEvent(Player player, Location location, String blockWord, Boolean isHit, Double score)
    {
        this.player = player;
        this.location = location;
        this.blockWord = blockWord;
        this.isHit = isHit;
        this.score = score;
    }

    public LetterBlocksEvent(Player player, Location location, String blockWord, Boolean isHit) {
        this(player, location, blockWord, isHit, 0.0);
    }

    public LetterBlocksEvent(Player player, Location location, String blockWord) {
        this(player, location, blockWord, false);
    }

    public @NotNull @Override HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
