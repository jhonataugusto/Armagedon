package br.com.hub.lobby.practice.commands.duel;

import br.com.core.crud.redis.DuelRedisCRUD;
import br.com.core.crud.redis.ServerRedisCRUD;
import br.com.core.data.DuelData;
import br.com.core.data.ServerData;
import br.com.core.enums.server.Server;
import br.com.hub.user.User;
import br.com.hub.user.request.DuelRequest;
import br.com.hub.util.bungee.BungeeUtils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

import static br.com.hub.util.scheduler.SchedulerUtils.async;
import static br.com.hub.util.scheduler.SchedulerUtils.sync;

@CommandAlias("acceptduel|acceptMatch|acceptX1|acceptChallenge|acceptDuelRequest")
@Description("Aceita um duelo de um jogador")
public class AcceptDuelRequestCommand extends BaseCommand {


    @Default
    public void onAccept(Player requested, String challengerUuid, String requestId) {

        Player challenger = Bukkit.getPlayer(UUID.fromString(challengerUuid));

        if (challenger == null) {
            requested.sendMessage(ChatColor.RED + "O jogador não está no servidor");
            return;
        }

        User challengerUser = User.fetch(challenger.getUniqueId());
        User requestedUser = User.fetch(requested.getUniqueId());

        if (challengerUser == null) {
            requested.sendMessage(ChatColor.RED + "O jogador não existe.");
            return;
        }

        if (requestedUser.getRequestById(requestId) == null) {
            requested.sendMessage(ChatColor.RED + "Esse duelo não existe ou já expirou");
            return;
        }

        if (requestedUser.getRequestById(requestId).hasExpired()) {
            requested.sendMessage(ChatColor.RED + "Esse duelo já expirou");
            return;
        }

        ServerData practiceServer = ServerRedisCRUD.findByName(Server.PRACTICE);

        if (practiceServer == null) {
            String serverNotExistsMessage = ChatColor.RED + "O servidor não existe";
            requested.sendMessage(serverNotExistsMessage);
            challengerUser.getPlayer().sendMessage(serverNotExistsMessage);
            return;
        }

        if (!practiceServer.isOnline()) {
            String offlineServerMessage = ChatColor.RED + "O servidor não está ligado.";
            requested.sendMessage(offlineServerMessage);
            challengerUser.getPlayer().sendMessage(offlineServerMessage);
            return;
        }

        DuelRequest request = requestedUser.getRequestById(requestId);

        DuelData duel = new DuelData();

        duel.getTeam1().add(requested.getUniqueId());
        duel.getTeam2().add(challenger.getUniqueId());

        duel.setCustom(true);
        duel.setRanked(false);
        duel.setGameModeName(request.getMode().getName());
        duel.setMapName(request.getMapName());

        DuelRedisCRUD.save(duel);

        requestedUser.getDuelRequests().remove(request);

        sync(() -> {
            BungeeUtils.connect(requested.getPlayer(), Server.PRACTICE);
            BungeeUtils.connect(challenger.getPlayer(), Server.PRACTICE);
        });
    }

}
