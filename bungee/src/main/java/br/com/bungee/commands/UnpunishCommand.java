package br.com.bungee.commands;

import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.data.AccountData;
import br.com.core.data.object.PunishmentDAO;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static br.com.bungee.util.scheduler.SchedulerUtils.async;

@CommandAlias("unban|unpunish|desbanir|desban|pardon|perdoar")
@Description("desbane um jogador")
public class UnpunishCommand extends BaseCommand {

    @Default
    public void onUnpunish(ProxiedPlayer player, String targetName) {

        AccountData targetAccount = AccountMongoCRUD.get(targetName);

        Account account = Account.fetch(player.getUniqueId());

        Rank rankSender = account.getRank();

        if (!Rank.getStaffers().contains(rankSender)) {
            return;
        }

        if (targetAccount == null) {
            player.sendMessage(ChatColor.RED + "Este jogador não existe");
            return;
        }

        PunishmentDAO activePunishment = targetAccount.getPunishments().stream().filter(PunishmentDAO::isActive).findFirst().orElse(null);

        if (activePunishment == null) {
            player.sendMessage(ChatColor.RED + "Este jogador não tem punições ativas.");
            return;
        }

        activePunishment.setActive(false);

        async(targetAccount::saveData);

        player.sendMessage(ChatColor.GREEN + "O jogador " + targetName + " foi desbanido com sucesso.");
    }
}
