package br.com.practice.events.arena.state;

import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class ArenaChangeStateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private ArenaStage stage;
    private ChangeCause cause;

    public ArenaChangeStateEvent(Arena arena, ArenaStage stage, ChangeCause cause) {
        this.arena = arena;
        this.stage = stage;
        this.cause = cause;
    }

    public ArenaChangeStateEvent(Arena arena, ArenaStage stage) {
        this.arena = arena;
        this.stage = stage;
        this.cause = ChangeCause.DEFAULT;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum ChangeCause {
        DEFAULT, TIME_EXCEED;
    }
}
