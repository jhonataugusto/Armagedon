package br.com.armagedon.listeners;

import br.com.armagedon.data.ServerData;
import br.com.armagedon.events.HeartBeatEvent;
import br.com.armagedon.server.Server;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerListener implements Listener {

    @EventHandler
    public void onHeartBeart(HeartBeatEvent event) {
        event.getServerInfoMap().forEach((name, serverInfo) -> {
            serverInfo.ping((ping, error) -> {

                Server server;

                if (error == null) {
                    int onlinePlayers = ping.getPlayers().getOnline();

                    server = new Server(new ServerData(name, true, onlinePlayers));

                } else {
                    server = new Server(new ServerData(name, false,0));
                }

                server.getData().save();
            });
        });
        System.out.println("coração batendo...");
    }
}
