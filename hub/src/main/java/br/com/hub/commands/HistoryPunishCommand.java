package br.com.hub.commands;

import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.data.AccountData;
import br.com.hub.gui.punish.PunishHistory;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;


@CommandAlias("historypunish|hpunish|punishhistory|punishhis|historybans|historyban|banhistory|bans|punishes|punishs")
@Description("verifica o histórico de banimentos do jogador.")
public class HistoryPunishCommand extends BaseCommand {

    @Default
    public void onAccessHistoryPunish(Player player, @Single String targetName) {

        AccountData targetAccount = AccountMongoCRUD.get(targetName);

        Account account = Account.fetch(player.getUniqueId());

        Rank rankSender = account.getRank();

        if (!Rank.getStaffers().contains(rankSender)) {
            return;
        }

        if (targetAccount == null) {
            player.sendMessage(ChatColor.RED + "Esse jogador não existe em nosso banco de dados.");
            return;
        }

        new PunishHistory(targetAccount).INVENTORY.open(player);
    }

}
