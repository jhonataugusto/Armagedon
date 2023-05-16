package br.com.bungee.commands;

import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.core.enums.server.Server;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@CommandAlias("start|estarte|iniciar|ligar|abrir|open")
public class StartCommand extends BaseCommand {

    @Default
    public void onStart(ProxiedPlayer player, @Single String serverName) {

        Account account = Account.fetch(player.getUniqueId());

        Rank accountRank = account.getRank();

        List<Rank> executiveStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.EXECUTIVE);

        if (!executiveStaff.contains(accountRank)) {
            return;
        }

        File serverPath = new File(System.getProperty("user.home") + File.separator + "armagedon", serverName);

        if (!serverPath.exists()) {
            player.sendMessage(ChatColor.RED + "O diretório " + serverPath.getAbsolutePath() + " não existe");
            return;
        }

        try {
            Process createScreenProcess = new ProcessBuilder().command("screen", "-dmS", serverName, "./start.sh").directory(serverPath).start();
            createScreenProcess.waitFor();
            player.sendMessage(ChatColor.GREEN + "Servidor " + serverName + " aberto com sucesso.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
