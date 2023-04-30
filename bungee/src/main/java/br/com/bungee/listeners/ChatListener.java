package br.com.bungee.listeners;

import br.com.core.account.Account;
import br.com.core.account.rank.Rank;
import br.com.core.enums.server.Server;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        Account account = Account.fetch(player.getUniqueId());

        if (account.getData() == null) {
            return;
        }

        ServerInfo senderServerInfo = player.getServer().getInfo();
        Server senderServer = Server.getByName(senderServerInfo.getName());

        Rank rank = Rank.getById(account.getRank().getId());

        TextComponent nameComponent;

        if (rank.equals(Rank.MEMBER)) {
            nameComponent = new TextComponent(rank.getColor() + rank.getChatDisplay() + player.getName());
        } else {
            nameComponent = new TextComponent(rank.getColor() + rank.getChatDisplay() + " " + player.getName());
        }

        TextComponent colon = new TextComponent(ChatColor.WHITE + ": ");
        TextComponent messageComponent = new TextComponent(ChatColor.WHITE + event.getMessage());
        BaseComponent[] message = new BaseComponent[]{nameComponent, colon, messageComponent};

        boolean isCommand = event.getMessage().startsWith("/");

        for (ProxiedPlayer targetPlayer : ProxyServer.getInstance().getPlayers()) {
            ServerInfo serverInfo = targetPlayer.getServer().getInfo();
            Server recipientServer = Server.getByName(serverInfo.getName());

            if (!senderServer.canSend(recipientServer)) {
                continue;
            }

            if (!isCommand) {
                targetPlayer.sendMessage(message);
            } else {

                if (targetPlayer == player) {
                    targetPlayer.sendMessage(message);
                }
            }
        }
    }
}
