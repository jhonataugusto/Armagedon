package br.com.practice.events.user;

import br.com.practice.arena.Arena;
import br.com.practice.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


@Getter
public class UserDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Arena arena;
    private User dead, killer;
    private boolean lastMember;
    private EntityDamageByEntityEvent event;

    public UserDeathEvent(Arena arena, User dead, User killer, boolean lastMember, EntityDamageByEntityEvent event) {
        this.arena = arena;
        this.dead = dead;
        this.killer = killer;
        this.lastMember = lastMember;
        this.event = event;
    }

    public UserDeathEvent(Arena arena, User dead, User killer, boolean lastMember) {
        this.arena = arena;
        this.dead = dead;
        this.lastMember = lastMember;
        this.killer = killer;
    }

    public UserDeathEvent(Arena arena, User dead, User killer) {
        this.arena = arena;
        this.dead = dead;
        this.killer = killer;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
