package br.com.practice.events.user;

import br.com.practice.arena.Arena;
import br.com.practice.user.User;
import br.com.practice.util.cuboid.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class UserExitArenaBoundsEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private User user;
    private Arena arena;
    private Cuboid bounds;

    public UserExitArenaBoundsEvent(User user, Arena arena, Cuboid bounds) {
        this.user = user;
        this.arena = arena;
        this.bounds = bounds;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
