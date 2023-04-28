package br.com.practice.events.arena.pulse;

import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ArenaPulseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Map<String, Arena> arenas;

    private long time;

    public ArenaPulseEvent(){
        this.arenas = Practice.getInstance().getArenaStorage().getArenas();
        this.time = System.nanoTime();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
