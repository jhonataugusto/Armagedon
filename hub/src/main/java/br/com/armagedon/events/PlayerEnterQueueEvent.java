package br.com.armagedon.events;

import br.com.armagedon.lobby.practice.queue.properties.QueueProperties;
import br.com.armagedon.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class PlayerEnterQueueEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private User user;
    private QueueProperties properties;

    public PlayerEnterQueueEvent(User user, QueueProperties properties) {
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
