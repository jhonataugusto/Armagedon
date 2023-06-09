package br.com.bungee.events;

import br.com.bungee.Bungee;
import br.com.core.enums.server.Server;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

@Getter
@Setter
@Accessors(chain = true)
public class ServerChatUnpairEvent extends Event {
    private ProxiedPlayer sender;
    private String message;
    private Server server;

    public void call(){
        Bungee.getInstance().getProxy().getPluginManager().callEvent(this);
    }
}
