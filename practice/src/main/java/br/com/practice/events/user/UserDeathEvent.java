package br.com.practice.events.user;

import br.com.practice.arena.Arena;
import br.com.practice.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Getter
@AllArgsConstructor
public class UserDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Arena arena;
    private User dead, killer;
    private boolean lastMember;


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
