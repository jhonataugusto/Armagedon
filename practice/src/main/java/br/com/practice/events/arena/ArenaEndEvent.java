package br.com.practice.events.arena;

import br.com.practice.arena.Arena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class ArenaEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Arena arena;

    public ArenaEndEvent(Arena arena) {
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
