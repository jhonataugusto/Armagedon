package br.com.hub.commands;

import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.hub.user.User;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("construir|build|buildar|construcao")
@Description("ativa o modo de construção")
public class BuildCommand extends BaseCommand {

    @Default
    public void onBuild(Player player) {
        User user = User.fetch(player.getUniqueId());

        List<Rank> executiveStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.EXECUTIVE);
        List<Rank> managerialStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.MANAGERIAL);

        Rank userRank = user.getAccount().getRank();

        if (!executiveStaff.contains(userRank) && !managerialStaff.contains(userRank)) {
            return;
        }

        user.setBuilding(!user.isBuilding());
        user.getPlayer().sendMessage(user.isBuilding() ? ChatColor.GREEN + "Modo de construção ativado" : ChatColor.RED + "Modo de construção desativado.");
    }
}
