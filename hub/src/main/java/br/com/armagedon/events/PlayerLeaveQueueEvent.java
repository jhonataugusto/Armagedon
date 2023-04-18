package br.com.armagedon.events;

import br.com.armagedon.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class PlayerLeaveQueueEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private User user;
    private br.com.armagedon.lobby.practice.queue.properties.QueueProperties properties;

    public PlayerLeaveQueueEvent(User user, br.com.armagedon.lobby.practice.queue.properties.QueueProperties properties) {
        this.user = user;
        this.properties = properties;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
