package br.com.hub.events;

import br.com.hub.lobby.practice.queue.properties.DuelProperties;
import br.com.hub.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class QueueMatchEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private User player1, player2;
    private DuelProperties properties;

    public QueueMatchEvent(User player1, User player2, DuelProperties properties) {
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
