package br.com.practice.commands;

import br.com.core.account.enums.rank.Rank;
import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.user.User;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

@CommandAlias("arenas")
@Description("lista todas as arenas, disponíveis ou não.")
public class ArenasCommand extends BaseCommand {

    @Default
    public void onArenaList(CommandSender sender) {

        Map<String, Arena> arenas = Practice.getInstance().getArenaStorage().getArenas();

        if (sender instanceof Player) {

            User userSender = User.fetch(((Player) sender).getPlayer().getUniqueId());
            Rank rankSender = userSender.getAccount().getRank();

            if (!Rank.getStaffers().contains(rankSender)) {
                return;
            }

            List<Rank> managerialStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.MANAGERIAL);
            List<Rank> executiveStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.EXECUTIVE);

            if (!managerialStaff.contains(rankSender) && !executiveStaff.contains(rankSender)) {
                return;
            }

            for (Arena arena : arenas.values()) {
                userSender.getPlayer().sendMessage("arena: " + arena.getMap().getId() + " | " + arena.getStage().name() + " | members: " + ((arena.getTeams().get(0) == null || arena.getTeams().get(1) == null) ? "null" : arena.getAllTeamMembers().size()));
            }

        } else {

            for (Arena arena : arenas.values()) {
                System.out.println("arena: " + arena.getMap().getId() + " | " + arena.getStage().name() + " | members: " + ((arena.getTeams().get(0) == null || arena.getTeams().get(1) == null) ? "null" : arena.getAllTeamMembers().size()));
            }

        }
    }
}
