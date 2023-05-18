package br.com.bungee.listeners;

import br.com.bungee.Bungee;
import br.com.core.account.enums.rank.Rank;
import br.com.core.data.object.PunishmentDAO;
import br.com.core.data.object.RankDAO;
import br.com.core.utils.motd.Motd;
import br.com.core.Core;
import br.com.core.account.Account;
import br.com.core.crud.redis.ServerRedisCRUD;
import br.com.core.data.ServerData;
import br.com.bungee.events.ProxyPulseEvent;
import br.com.bungee.server.Server;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.Random;

import static br.com.bungee.util.scheduler.SchedulerUtils.async;

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

                server.getData().saveData();
            });
        });
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Account account = new Account(player.getUniqueId());

        if (account.getName() == null) {
            account.getData().setName(player.getDisplayName());
        }

        Bungee.getInstance().getAccountStorage().register(account.getUuid(), account);

        RankDAO rankDAO = account.getData().getRanks().stream().findFirst().get();
        boolean hasExpiredRank = rankDAO.getExpiration() < System.currentTimeMillis() && rankDAO.getExpiration() != -1;

        PunishmentDAO punishment = account.getData().getPunishments().stream().filter(PunishmentDAO::isActive).findFirst().orElse(null);
        boolean hasActivePunishment = punishment != null;

        if (hasActivePunishment) {
            long expiration = Long.parseLong(punishment.getExpiration());

            if (expiration >= System.currentTimeMillis() || expiration == -1) {

                TextComponent headerComponent = new TextComponent(ChatColor.RED + "\n§lVocê foi banido\n\n".toUpperCase());
                TextComponent idComponent = new TextComponent(ChatColor.YELLOW + "Banimento: " + punishment.getId() + "\n");
                TextComponent punishDurationComponent = new TextComponent(ChatColor.YELLOW + "Data de expiração: §r" + ChatColor.RED + (expiration > 0 ? Core.DATE_FORMAT.format(new Date(expiration)) : "PERMANENTE") + "\n");
                TextComponent reasonComponent = new TextComponent(ChatColor.YELLOW + "Razão: §r" + punishment.getReason() + "\n\n");
                TextComponent appealComponent = new TextComponent(ChatColor.AQUA + "Foi banido incorretamente? peça appeal em nosso discord: §r " + "§ldiscord.io/armaggedon".toUpperCase());

                BaseComponent[] message = new BaseComponent[]{headerComponent, idComponent, punishDurationComponent, reasonComponent, appealComponent};

                player.disconnect(message);
                return;
            } else {
                punishment.setActive(false);
                player.sendMessage(ChatColor.YELLOW + "Seu banimento acabou. Divirta-se jogando!");
            }

        }

        if (hasExpiredRank) {
            account.setRank(Rank.MEMBER, -1L);
            player.sendMessage(ChatColor.GRAY + "Seu rank expirou, você retornou ao rank padrão.");
        }

        account.getData().setCurrentDuelUuid(null);

        async(account.getData()::saveData);
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        Account account = Account.fetch(player.getUniqueId());

        Bungee.getInstance().getAccountStorage().unregister(account.getUuid());
    }

    @EventHandler
    public void onServerPing(ProxyPingEvent event) {
        ServerPing ping = event.getResponse();

        String randomText = Motd.MOTDS.get(new Random().nextInt(Motd.MOTDS.size()));

        BaseComponent motdLine = new TextComponent(ChatColor.AQUA + "§l  " + Core.SERVER_NAME.toUpperCase() + " [BETA-" + Core.SERVER_VERSION + "] " + "» §r" + Core.SERVER_WEBSITE);

        motdLine.addExtra("\n§r" + ChatColor.WHITE + randomText);

        ping.setDescriptionComponent(motdLine);
        ping.setVersion(new ServerPing.Protocol("Versão do Servidor", ping.getVersion().getProtocol()));

        event.setResponse(ping);
    }

}
