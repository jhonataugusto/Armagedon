package br.com.armagedon.listeners;

import br.com.armagedon.Bungee;
import br.com.armagedon.account.Account;
import br.com.armagedon.data.ServerData;
import br.com.armagedon.events.HeartBeatEvent;
import br.com.armagedon.server.Server;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
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
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Account account = new Account(player.getUniqueId());

        Bungee.getInstance().getAccountStorage().register(account.getUuid(), account);
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Account account = Account.fetch(player.getUniqueId());

        Bungee.getInstance().getAccountStorage().unregister(account.getUuid());
    }
}
