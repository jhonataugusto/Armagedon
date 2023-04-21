package br.com.armagedon.events.user;

import br.com.armagedon.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Getter
@AllArgsConstructor
public class UserDeathEvent extends Event {
    private User dead, killer;
    private boolean left;

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
