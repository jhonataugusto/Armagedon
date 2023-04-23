package br.com.armagedon.listener;

import br.com.armagedon.Practice;
import br.com.armagedon.account.Account;
import br.com.armagedon.arena.Arena;
import br.com.armagedon.data.DuelContextData;
import br.com.armagedon.enums.game.GameMode;
import br.com.armagedon.game.Game;
import br.com.armagedon.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Account account = Account.fetch(player.getUniqueId());

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

            Arena newArena = game.createArena();
            newArena.setData(duelContext);
            newArena.handleJoin(user);

        } else {

            arena.setData(duelContext);
            arena.handleJoin(user);
        }
    }
}
