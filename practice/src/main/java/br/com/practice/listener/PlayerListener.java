package br.com.practice.listener;

import br.com.core.crud.redis.DuelContextRedisCRUD;
import br.com.practice.Practice;
import br.com.core.account.Account;
import br.com.practice.arena.Arena;
import br.com.core.data.DuelData;
import br.com.core.enums.game.GameMode;
import br.com.practice.game.Game;
import br.com.practice.spectator.Spectator;
import br.com.practice.user.User;
import br.com.practice.util.tag.TagUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static br.com.practice.util.scheduler.SchedulerUtils.*;

public class PlayerListener implements Listener {


    @EventHandler
    public void onPlayerJoinMessage(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuitEventMessage(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        sync(() -> {
            Player player = event.getPlayer();
            User user = new User(player.getUniqueId(), player);
            Practice.getInstance().getUserStorage().register(user.getUuid(), user);

            DuelData duelSpectator = DuelData.getSpectatorContext(user.getAccount());

            if (duelSpectator != null && duelSpectator.getArenaId() != null) {
                Arena arena = Practice.getInstance().getArenaStorage().getArena(duelSpectator.getArenaId());
                Spectator.spectate(player, arena);
                return;
            }

            DuelData duel = DuelData.getContext(user.getAccount());

            if (duel == null) {
                player.kickPlayer(ChatColor.RED + "Não há duelos disponíveis para essa conta.");
                return;
            }

            GameMode gameMode = GameMode.getByName(duel.getGameModeName());
            Game game = Practice.getInstance().getGameStorage().getGame(gameMode);

            Arena loadedArena = Practice.getInstance().getArenaStorage().getArena(duel.getArenaId());

            if (loadedArena != null) {
                loadedArena.handleJoin(user);

            } else {
                Arena newArena = Practice.getInstance().getArenaStorage().getFreeArena(game, duel);

                if (newArena == null) {
                    player.kickPlayer(ChatColor.RED + "Não há salas disponíveis no momento, tente novamente.");

                } else {

                    duel.setArenaId(newArena.getId());
                    newArena.setData(duel);
                    newArena.handleJoin(user);
                }
            }
        });
    }
}

