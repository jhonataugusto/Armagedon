package br.com.armagedon.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ServerHeartBeatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public ServerHeartBeatEvent() {

    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
