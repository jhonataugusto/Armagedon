package br.com.hub.lobby.practice.commands;

import br.com.core.account.Account;
import br.com.hub.gui.challenge.ChallengeMapSelectorGUI;
import br.com.hub.gui.challenge.ChallengeModeSelectorGUI;
import br.com.hub.user.User;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandAlias("duel|duelar|x1")
@Description("Chama seu adversário para um duelo")
public class DuelCommand extends BaseCommand {

    @Default
    public void onDuel(Player sender, @Single String targetName) {
        Player target = Bukkit.getPlayer(targetName);

        if(target == null) {
            sender.sendMessage(ChatColor.RED + "O jogador está offline");
            return;
        }

        if(target.equals(sender)) {
            sender.sendMessage(ChatColor.RED + "Você não pode duelar a sí mesmo.");
            return;
        }


        User targetUser = User.fetch(target.getUniqueId());



        if(targetUser.getAccount().getData().getCurrentDuelContextUuid() != null) {
            sender.sendMessage(ChatColor.RED + "O jogador já está em um duelo.");
            return;
        }



        new ChallengeModeSelectorGUI(targetUser).INVENTORY.open(sender);
    }
}
