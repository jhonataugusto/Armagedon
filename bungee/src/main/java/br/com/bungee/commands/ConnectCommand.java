package br.com.bungee.commands;


import br.com.bungee.Bungee;
import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.core.enums.server.Server;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

import static br.com.bungee.util.scheduler.SchedulerUtils.async;

@CommandAlias("conectar|connect|server|go|ir|")
@Description("se conectar em qualquer servidor")
public class ConnectCommand extends BaseCommand {

    @Default
    public void onConnect(ProxiedPlayer sender, @Single String serverName) {

        Account account = Account.fetch(sender.getUniqueId());

        Rank accountRank = account.getRank();

        List<Rank> executiveStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.EXECUTIVE);

        if (!executiveStaff.contains(accountRank)) {
            return;
        }

        Server server = Server.getByName(serverName);

        if (server == null) {
            sender.sendMessage(ChatColor.RED + "O servidor não existe.");
            return;
        }

        ServerInfo serverInfo = Bungee.getInstance().getProxy().getServerInfo(serverName);

        async(() -> sender.connect(serverInfo));
    }
}
