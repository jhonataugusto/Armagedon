package br.com.armagedon.events.arena;

import br.com.armagedon.arena.Arena;
import br.com.armagedon.arena.ArenaStage;
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

    public ArenaChangeStateEvent(Arena arena, ArenaStage stage) {
        this.arena = arena;
        this.stage = stage;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

}
