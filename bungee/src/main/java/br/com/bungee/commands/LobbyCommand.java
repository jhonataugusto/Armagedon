package br.com.bungee.commands;

import br.com.bungee.Bungee;
import br.com.core.enums.server.Server;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static br.com.bungee.util.scheduler.SchedulerUtils.async;

@CommandAlias("lobby|l|hub")
@Description("teleporta o jogador para o lobby")
public class LobbyCommand extends BaseCommand {

    @Default
    public void onLobbyTeleport(ProxiedPlayer player) {
        ServerInfo lobby = Bungee.getInstance().getProxy().getServerInfo(Server.LOBBY.getName());
        async(() -> player.connect(lobby));
    }

}
