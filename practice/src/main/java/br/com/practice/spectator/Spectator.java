package br.com.practice.spectator;

import br.com.core.enums.server.Server;
import br.com.practice.arena.Arena;
import br.com.practice.events.spectator.SpectatorEnterArenaEvent;
import br.com.practice.events.spectator.SpectatorLeaveArenaEvent;
import br.com.practice.user.User;
import br.com.practice.util.bungee.BungeeUtils;
import br.com.practice.util.scheduler.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static br.com.practice.util.scheduler.SchedulerUtils.async;
import static br.com.practice.util.scheduler.SchedulerUtils.sync;

public class Spectator {

    public static void spectate(Player spectator, Arena arenaSpectated) {

        User userSpectator = User.fetch(spectator.getUniqueId());

        Bukkit.getServer().getPluginManager().callEvent(new SpectatorEnterArenaEvent(arenaSpectated, userSpectator));
    }


    public static void unspectate(Player unspectator, Arena arenaUnspectated) {
        User userUnspectator = User.fetch(unspectator.getUniqueId());

        sync(() -> BungeeUtils.connect(unspectator, Server.LOBBY_PRACTICE));

        Bukkit.getServer().getPluginManager().callEvent(new SpectatorLeaveArenaEvent(arenaUnspectated, userUnspectator));
    }
}
