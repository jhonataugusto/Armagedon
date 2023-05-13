package br.com.hub.commands;

import br.com.core.account.enums.rank.Rank;
import br.com.hub.user.User;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandAlias("construir|build|buildar|construcao")
@Description("ativa o modo de construção")
public class BuildCommand extends BaseCommand {

    @Default
    public void onBuild(Player player) {
        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        if (user.getAccount().getRank().isAboveOrEquals(Rank.SENIOR_ADMINISTRATOR)) {
            return;
        }

        user.setBuilding(!user.isBuilding());
        user.getPlayer().sendMessage(user.isBuilding() ? ChatColor.GREEN + "Modo de construção ativado" : ChatColor.RED + "Modo de construção desativado.");
    }
}
