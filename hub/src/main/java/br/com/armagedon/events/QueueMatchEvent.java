package br.com.armagedon.events;

import br.com.armagedon.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class QueueMatchEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private User player1, player2;
    private br.com.armagedon.lobby.practice.queue.properties.QueueProperties properties;

    public QueueMatchEvent(User player1, User player2, br.com.armagedon.lobby.practice.queue.properties.QueueProperties properties) {
        this.player1 = player1;
        this.player2 = player2;
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
