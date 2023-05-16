package br.com.practice.commands;

import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

@CommandAlias("arenas")
@Description("lista todas as arenas, disponíveis ou não.")
public class ArenasCommand extends BaseCommand {

    @Default
    public void onArenaList(CommandSender sender) {

        if (sender instanceof Player) {
            return;
        }

        Map<String, Arena> arenas = Practice.getInstance().getArenaStorage().getArenas();

        for (Arena arena : arenas.values()) {
            System.out.println("arena: " + arena.getMap().getId() + " | " + arena.getStage().name() + " | members: " + ((arena.getTeams().get(0) == null || arena.getTeams().get(1) == null) ? "null" : arena.getAllTeamMembers().size()));
        }
    }
}
