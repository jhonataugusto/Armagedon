package br.com.practice.events.spectator;

import br.com.practice.arena.Arena;
import br.com.practice.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class SpectatorEnterArenaEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Arena arena;
    private User spectator;

    public SpectatorEnterArenaEvent(Arena arena, User spectator) {
        this.arena = arena;
        this.spectator = spectator;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
