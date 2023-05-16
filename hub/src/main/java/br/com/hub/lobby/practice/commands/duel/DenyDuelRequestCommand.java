package br.com.hub.lobby.practice.commands.duel;

import br.com.hub.user.User;
import br.com.hub.user.request.DuelRequest;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("denyduel|denyMatch|denyX1|denyChallenge|denyDuelRequest")
@Description("Recusa um duelo de um jogador")
public class DenyDuelRequestCommand extends BaseCommand {

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

        DuelRequest request = requestedUser.getRequestById(requestId);

        request.setExpiration(-1L);

        challengerUser.getPlayer().sendMessage(" ");
        challengerUser.getPlayer().sendMessage(ChatColor.RED + requestedUser.getName() + " recusou a sua solicitação de duelo.");
        challengerUser.getPlayer().sendMessage(" ");

        requestedUser.getPlayer().sendMessage(" ");
        requestedUser.getPlayer().sendMessage(ChatColor.RED + "Você recusou a solicitação de " + challengerUser.getName());
        requestedUser.getPlayer().sendMessage(" ");
    }
}
