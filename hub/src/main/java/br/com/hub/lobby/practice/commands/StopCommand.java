package br.com.hub.lobby.practice.commands;


import br.com.core.account.enums.rank.Rank;
import br.com.core.enums.server.Server;

import br.com.hub.Hub;
import br.com.hub.user.User;
import br.com.hub.util.bungee.BungeeUtils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static br.com.hub.util.scheduler.SchedulerUtils.async;
import static br.com.hub.util.scheduler.SchedulerUtils.delay;


@CommandAlias("stop|parar|desligar")
@Description("desliga qualquer servidor")
public class StopCommand extends BaseCommand {

    @Default
    public void onStop(Player sender) {

        User user = User.fetch(sender.getUniqueId());

        List<Rank> executiveStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.EXECUTIVE);
        List<Rank> managerialStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.MANAGERIAL);

        Rank userRank = user.getAccount().getRank();

        if (!executiveStaff.contains(userRank) && !managerialStaff.contains(userRank)) {
            return;
        }

        Bukkit.getServer().getOnlinePlayers().forEach(players -> {
           async(() -> BungeeUtils.connect(players, Server.LOBBY));
        });

        delay(() -> {
            Hub.getInstance().onDisable();
            Bukkit.shutdown();
        }, 2);
    }
}
