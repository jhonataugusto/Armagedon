package br.com.practice.events.arena.statistic;

import br.com.practice.arena.Arena;
import br.com.practice.arena.team.ArenaTeam;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class ArenaWinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private ArenaTeam winnerTeam;
    private ArenaTeam loserTeam;

    public ArenaWinEvent(Arena arena, ArenaTeam winnerTeam, ArenaTeam loserTeam) {
        this.arena = arena;
        this.winnerTeam = winnerTeam;
        this.loserTeam = loserTeam;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
