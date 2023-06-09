package br.com.hub.util.tag;

import br.com.core.account.enums.rank.Rank;
import br.com.hub.Hub;
import br.com.hub.user.User;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class TagUtil {

    public static void loadTag(Player player, Rank rank) {
        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        Scoreboard scoreboard = Hub.getInstance().getLobby().getLobbyScoreboard().toBukkitScoreboard();

        String playerName = player.getName();

        Team team = scoreboard.getEntryTeam(playerName);

        if (team != null) {
            team.unregister();
        }

        team = scoreboard.registerNewTeam(playerName);
        team.setPrefix(rank.getColor());
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
