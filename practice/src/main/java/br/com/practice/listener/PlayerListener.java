package br.com.practice.listener;

import br.com.practice.Practice;
import br.com.core.account.Account;
import br.com.practice.arena.Arena;
import br.com.core.data.DuelContextData;
import br.com.core.enums.game.GameMode;
import br.com.practice.game.Game;
import br.com.practice.spectator.Spectator;
import br.com.practice.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoinMessage(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuitEventMessage(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHandleJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        Account account = Account.fetch(player.getUniqueId());

        DuelContextData duelContextSpectator = DuelContextData.getSpectatorContext(account);

        if (duelContextSpectator != null && duelContextSpectator.getArenaId() != null) {
            Arena arena = Practice.getInstance().getArenaStorage().getArena(duelContextSpectator.getArenaId());

            Spectator.spectate(player,arena);
            return;
        }

        DuelContextData duelContext = DuelContextData.getContext(account);

        if (duelContext == null) {
            player.kickPlayer(ChatColor.RED + "Não há nenhum duelo reservado para essa conta");
            return;
        }

        User user = new User(account.getUuid());
        Practice.getInstance().getUserStorage().register(user.getUuid(), user);

        GameMode gamemode = GameMode.getByName(duelContext.getGameMode());
        Game game = Practice.getInstance().getGameStorage().getGame(gamemode);

        Arena arena = Practice.getInstance().getArenaStorage().getFreeArena(game);

        if (arena == null) {

            Arena newArena = game.createArena(null);

            duelContext.setArenaId(newArena.getId());
            newArena.setData(duelContext);
            newArena.getData().saveData();

            newArena.handleJoin(user);

        } else {

            duelContext.setArenaId(arena.getId());
            arena.setData(duelContext);
            arena.getData().saveData();

            arena.handleJoin(user);
        }
    }
}
