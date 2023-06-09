package br.com.hub.events;

import br.com.hub.lobby.practice.queue.properties.DuelProperties;
import br.com.hub.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class PlayerLeaveQueueEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private User user;
    private DuelProperties properties;

    public PlayerLeaveQueueEvent(User user, DuelProperties properties) {
        this.user = user;
        this.properties = properties;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
