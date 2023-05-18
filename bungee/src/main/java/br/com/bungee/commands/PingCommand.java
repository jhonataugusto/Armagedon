package br.com.bungee.commands;

import br.com.bungee.Bungee;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@CommandAlias("ping|latencia|pingue|pong")
@Description("veja seu ping no servidor")
public class PingCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onPing(ProxiedPlayer player, @Single String targetName) {

        ProxiedPlayer target = Bungee.getInstance().getProxy().getPlayer(targetName);

        if (target == null || target.equals(player) || targetName.length() == 0) {
            player.sendMessage(ChatColor.GREEN + "Seu ping: " + player.getPing());
        } else {
            player.sendMessage(ChatColor.GREEN + "Ping de " + target.getName() + ": " + target.getPing());
        }
    }
}
