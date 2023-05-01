package br.com.hub.events;

import br.com.hub.Hub;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ServerPulseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Hub hub;

    public ServerPulseEvent(Hub hub) {
        this.hub = hub;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
