package br.com.bungee.listeners;

import br.com.bungee.Bungee;
import br.com.core.account.Account;
import br.com.core.crud.mongo.DuelContextMongoCRUD;
import br.com.core.crud.redis.ServerRedisCRUD;
import br.com.core.data.DuelData;
import br.com.core.data.ServerData;
import br.com.bungee.events.ProxyPulseEvent;
import br.com.bungee.server.Server;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ServerListener implements Listener {

    @EventHandler
    public void onHeartBeart(ProxyPulseEvent event) {
        event.getServerInfoMap().forEach((name, serverInfo) -> {
            serverInfo.ping((ping, error) -> {

                Server server;

                if (error == null) {
                    int onlinePlayers = ping.getPlayers().getOnline();

                    ServerData data = new ServerData();

                    data.setName(name);
                    data.setOnline(true);
                    data.setCurrentPlayers(onlinePlayers);

                    server = new Server(data);
                } else {
                    ServerData serverActualData = ServerRedisCRUD.findByName(name);
                    if (serverActualData == null) {
                        server = new Server(new ServerData());
                    } else {
                        serverActualData.setOnline(false);
                        server = new Server(serverActualData);
                    }

                }

                server.getData().save();
            });
        });
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Account account = new Account(player.getUniqueId());

        if (account.getName() == null) {
            account.getData().setName(player.getDisplayName());
            account.getData().saveData();
        }

        Bungee.getInstance().getAccountStorage().register(account.getUuid(), account);
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Account account = Account.fetch(player.getUniqueId());

        Bungee.getInstance().getAccountStorage().unregister(account.getUuid());
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {

        if(event.getFrom() == null) {
            return;
        }

        Account account = Account.fetch(event.getPlayer().getUniqueId());
        if (event.getFrom().getName().equals(br.com.core.enums.server.Server.PRACTICE.getName()) && account.getData().getCurrentDuelContextUuid() != null) {

            DuelData data = DuelContextMongoCRUD.get(UUID.fromString(account.getData().getCurrentDuelContextUuid()));

            if (data == null) {
                return;
            }

            String command = "match " + data.getUuid();

            TextComponent component = new TextComponent(ChatColor.GREEN + "Clique aqui para ver o resultado da partida!");
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
            event.getPlayer().sendMessage(component);

            account.getData().setCurrentDuelContextUuid(null);
            account.getData().saveData();
        }
    }
}
