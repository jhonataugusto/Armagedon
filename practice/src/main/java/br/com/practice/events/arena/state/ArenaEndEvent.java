package br.com.practice.events.arena.state;

import br.com.practice.arena.Arena;
import br.com.practice.arena.team.ArenaTeam;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class ArenaEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Arena arena;
    private ArenaTeam teamWinner;
    private ArenaTeam teamLoser;

    public ArenaEndEvent(Arena arena, ArenaTeam teamWinner, ArenaTeam teamLoser) {
        this.arena = arena;
        this.teamWinner = teamWinner;
        this.teamLoser = teamLoser;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
