package br.com.bungee.events;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Event;

import java.util.Map;


@Getter
@Setter
public class
ProxyPulseEvent extends Event {

    Map<String, ServerInfo> serverInfoMap;


    public ProxyPulseEvent() {
        serverInfoMap = ProxyServer.getInstance().getServers();
    }

}
