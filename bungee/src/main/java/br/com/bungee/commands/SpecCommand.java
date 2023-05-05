package br.com.bungee.commands;

import br.com.core.account.Account;
import br.com.core.data.DuelData;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

@CommandAlias("spec|spectar|spectate|assistir")
@Description("Assista a partida dos jogadores")
public class SpecCommand extends BaseCommand {

    @Default
    public void onSpec(ProxiedPlayer sender, @Single String playerName) {

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(playerName);

        if (!sender.getServer().getInfo().getName().equals(br.com.core.enums.server.Server.LOBBY_PRACTICE.getName())) {
            return;
        }

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Jogador não encontrado!");
            return;
        }

        if (target.equals(sender)) {
            sender.sendMessage(ChatColor.RED + "Você não pode assistir a sí mesmo.");
            return;
        }

        Account account = Account.fetch(target.getUniqueId());
        DuelData data = DuelData.getContext(account);

        if (data == null || target.getServer().equals(sender.getServer())) {
            sender.sendMessage(ChatColor.RED + "O jogador não está em um duelo.");
            return;
        }

        Server server = target.getServer();

        if (!data.getSpectators().contains(sender.getUniqueId())) {
            data.getSpectators().add(sender.getUniqueId());
        }

        data.saveData();
        sender.connect(server.getInfo());
    }
}
