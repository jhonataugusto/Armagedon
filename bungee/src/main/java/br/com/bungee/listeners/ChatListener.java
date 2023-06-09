package br.com.bungee.listeners;

import br.com.bungee.Bungee;
import br.com.bungee.events.RedisPubSubEvent;
import br.com.bungee.events.ServerChatPairEvent;
import br.com.bungee.events.ServerChatUnpairEvent;
import br.com.core.Core;
import br.com.core.account.Account;
import br.com.core.account.enums.preferences.Preference;
import br.com.core.account.enums.rank.Rank;
import br.com.core.data.object.DiscordMessageDAO;
import br.com.core.data.object.PreferenceDAO;
import br.com.core.database.redis.RedisChannels;
import br.com.core.enums.server.Server;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.event.EventHandler;

import static br.com.core.utils.pubsub.RedisPubSubUtil.publish;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        Bungee.getInstance().getProxy().getServerInfo(Server.PRACTICE.getName()).getPlayers();

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

                new ServerChatUnpairEvent()
                        .setMessage(ComponentSerializer.toString(message))
                        .setServer(senderServer)
                        .setSender(player).call();

                continue;
            }

            if (!isCommand) {
                Account targetAccount = Account.fetch(targetPlayer.getUniqueId());
                PreferenceDAO preference = targetAccount.getData().getPreferenceByName(Preference.DISABLE_CHAT);

                if (preference.isActive()) {
                    return;
                }

                targetPlayer.sendMessage(message);
            } else {

                if (targetPlayer == player) {
                    targetPlayer.sendMessage(message);
                }
            }

            new ServerChatPairEvent()
                    .setMessage(ComponentSerializer.toString(message))
                    .setServer(senderServer)
                    .setSender(player).call();


        }

        DiscordMessageDAO discordMessageWrapped = new DiscordMessageDAO(player.getDisplayName(), event.getMessage());

        String messageJsonWrapped = Core.GSON.toJson(discordMessageWrapped);
        publish(RedisChannels.DISCORD_RECEIVE_MESSAGES_CHANNEL.getChannel(), messageJsonWrapped);
    }


    @EventHandler
    public void onReceiveMessageFromDiscord(RedisPubSubEvent event) {

        if (!event.getChannel().equalsIgnoreCase(RedisChannels.MINECRAFT_RECEIVE_MESSAGES_CHANNEL.getChannel())) {
            return;
        }

        DiscordMessageDAO discordMessageWrapper = Core.GSON.fromJson(event.getMessage(), DiscordMessageDAO.class);

        if (discordMessageWrapper == null) {
            return;
        }

        TextComponent nameComponent = new TextComponent(ChatColor.GRAY + discordMessageWrapper.getNick());
        TextComponent colon = new TextComponent(ChatColor.WHITE + ": ");
        TextComponent messageComponent = new TextComponent(ChatColor.WHITE + discordMessageWrapper.getMessage());
        BaseComponent[] message = new BaseComponent[]{nameComponent, colon, messageComponent};
        Bungee.getInstance().getProxy().broadcast(message);
    }
}
