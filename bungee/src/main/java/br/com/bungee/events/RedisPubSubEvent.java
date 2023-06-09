package br.com.bungee.events;

import br.com.bungee.Bungee;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.plugin.Event;

@Getter
@Setter
@Accessors(chain = true)
public class RedisPubSubEvent extends Event {
    private String channel, message;

    public void call(){
        Bungee.getInstance().getProxy().getPluginManager().callEvent(this);
    }
}
