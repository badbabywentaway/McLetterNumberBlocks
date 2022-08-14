package me.stephanosbad.edublocks.mcletternumberblocks.Items;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class NumberHitEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public final Player player;

    public final char hitChar;


    public NumberHitEvent(Player player, char hitChar)
    {
        this.player = player;
        this.hitChar = hitChar;
    }

    @NotNull @Override public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
