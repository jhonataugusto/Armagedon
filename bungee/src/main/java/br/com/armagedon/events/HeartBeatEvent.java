package br.com.armagedon.events;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Event;

import java.util.Map;


@Getter
@Setter
public class HeartBeatEvent extends Event {

    Map<String, ServerInfo> serverInfoMap;


    public HeartBeatEvent() {
        serverInfoMap = ProxyServer.getInstance().getServers();
    }

}
