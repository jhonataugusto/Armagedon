package br.com.practice.commands;


import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.core.enums.server.Server;
import br.com.practice.Practice;
import br.com.practice.user.User;
import br.com.practice.util.bungee.BungeeUtils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static br.com.practice.util.scheduler.SchedulerUtils.*;


@CommandAlias("stop|parar|desligar")
@Description("desliga qualquer servidor")
public class StopCommand extends BaseCommand {

    @Default
    public void onStop(Player sender) {

        User userSender = User.fetch(sender.getUniqueId());
        Rank rankSender = userSender.getAccount().getRank();

        if (!Rank.getStaffers().contains(rankSender)) {
            return;
        }

        List<Rank> managerialStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.MANAGERIAL);
        List<Rank> executiveStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.EXECUTIVE);

        if (!managerialStaff.contains(rankSender) && !executiveStaff.contains(rankSender)) {
            return;
        }

        Bukkit.getServer().getOnlinePlayers().forEach(players -> {
            async(() -> BungeeUtils.connect(players, Server.LOBBY));
        });

        delay(() -> {
            Practice.getInstance().onDisable();
            Bukkit.shutdown();
        }, 2);
    }
}
