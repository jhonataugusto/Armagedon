package br.com.practice.util.tag;

import br.com.practice.arena.team.ArenaTeam;
import br.com.practice.user.User;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TagUtil {

    public static void loadTag(User user, ArenaTeam arenaTeam) {

        if (user == null) {
            return;
        }

        if(user.getArena() == null) {
            return;
        }

        Scoreboard scoreboard = user.getArena().getGameScoreboard().toBukkitScoreboard();

        String playerName = user.getPlayer().getName();

        Team team = scoreboard.getEntryTeam(playerName);

        if (team != null) {
            team.unregister();
        }

        team = scoreboard.registerNewTeam(playerName);
        team.setPrefix(arenaTeam.getColor() + "");
        team.setSuffix("");
        team.addEntry(playerName);
    }

    public static void unloadTag(Player player) {
        String playerName = player.getName();
        Team team = player.getScoreboard().getTeam(playerName);

        if (team != null) {
            for (String entry : team.getEntries()) {
                player.getScoreboard().resetScores(entry);
            }
            team.unregister();
        }
    }
}
