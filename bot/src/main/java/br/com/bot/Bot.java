package br.com.bot;

import br.com.core.Core;
import br.com.core.data.object.DiscordMessageDAO;
import br.com.core.database.redis.RedisChannels;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import static br.com.core.utils.pubsub.RedisPubSubUtil.publish;
import static br.com.core.utils.pubsub.RedisPubSubUtil.subscribe;

public class Bot extends ListenerAdapter {

    public static void main(String[] args) {
        try {
            JDA jda = JDABuilder.createDefault("MTEwOTMzMzIwNDI0NDExMTM5MA.GGU95S.GWUgfN8p_oZC9uf98wpqOsGbHpPpa3G_NmPhnc")
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                    .addEventListeners(new Bot())
                    .build();

            subscribe(((channel, message) -> {
                TextChannel textChannel = jda.getTextChannelById("1109402411786842154");

                if (textChannel == null || message == null) {
                    return;
                }

                DiscordMessageDAO discordMessageWrapper = Core.GSON.fromJson(message, DiscordMessageDAO.class);

                if (discordMessageWrapper == null) {
                    return;
                }

                Member member = textChannel.getGuild().getSelfMember();

                member.modifyNickname(discordMessageWrapper.getNick()).queue();
                textChannel.sendMessage(discordMessageWrapper.getMessage()).queue();

            }), RedisChannels.DISCORD_RECEIVE_MESSAGES_CHANNEL.getChannel());

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //TODO: ele mostra comandos, não pode mostrar comandos!
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String channel = RedisChannels.MINECRAFT_RECEIVE_MESSAGES_CHANNEL.getChannel();

        if (!event.getAuthor().isBot()) {
            String messageContent = event.getMessage().getContentRaw();

            if (messageContent.startsWith("/")) {
                return;
            }

            DiscordMessageDAO discordMessageWrapper = new DiscordMessageDAO(event.getAuthor().getName(), messageContent);

            String json = Core.GSON.toJson(discordMessageWrapper);

            publish(channel, json);
        }
    }


}
