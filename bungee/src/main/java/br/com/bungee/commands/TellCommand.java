package br.com.bungee.commands;

import br.com.bungee.Bungee;
import br.com.core.account.Account;
import br.com.core.account.enums.preferences.Preference;
import br.com.core.data.object.PreferenceDAO;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static br.com.bungee.util.scheduler.SchedulerUtils.async;

@CommandAlias("tell|t|telefone|msg|mensagem|m")
@Description("manda uma mensagem privada para o jogador")
public class TellCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onTell(ProxiedPlayer sender, String targetName, String[] message) {

        ProxiedPlayer target = Bungee.getInstance().getProxy().getPlayer(targetName);

        if (target == null || !target.isConnected()) {
            sender.sendMessage(ChatColor.RED + "Esse jogador não existe ou está offline.");
            return;
        }

        if (target.equals(sender)) {
            sender.sendMessage(ChatColor.RED + "Você não pode mandar mensagem para você mesmo.");
            return;
        }

        Account targetAccount = Bungee.getInstance().getAccountStorage().getAccount(target.getUniqueId());

        Account senderAccount = Bungee.getInstance().getAccountStorage().getAccount(sender.getUniqueId());

        PreferenceDAO preference = targetAccount.getData().getPreferenceByName(Preference.DISABLE_PRIVATE_MESSAGES);
        PreferenceDAO senderPreference = senderAccount.getData().getPreferenceByName(Preference.DISABLE_PRIVATE_MESSAGES);

        if (preference.isActive()) {
            sender.sendMessage(ChatColor.RED + "Esse jogador está com as mensagens privadas desabilitadas.");
            return;
        } else if (senderPreference.isActive()) {
            sender.sendMessage(ChatColor.RED + "Você não pode enviar nem receber mensagens privadas.");
            return;
        }

        if (message == null || message.length == 0) {
            sender.sendMessage(ChatColor.RED + "A mensagem não pode ser vazia.");
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < message.length; i++) {

            if (i == message.length - 1) {
                builder.append(message[i]);
            } else {
                builder.append(message[i]).append(" ");
            }
        }

        TextComponent targetNameComponent = new TextComponent(ChatColor.GRAY + "(Para " + targetAccount.getRank().getColor() + targetAccount.getName() + ChatColor.GRAY + ") ");
        TextComponent targetMessageComponent = new TextComponent(ChatColor.GRAY + builder.toString());
        BaseComponent[] targetComponents = new BaseComponent[]{targetNameComponent, targetMessageComponent};

        sender.sendMessage(targetComponents);

        TextComponent senderNameComponent = new TextComponent(ChatColor.GRAY + "(De " + senderAccount.getRank().getColor() + senderAccount.getName() + ChatColor.GRAY + ") ");
        TextComponent senderMessageComponent = new TextComponent(ChatColor.GRAY + builder.toString());
        BaseComponent[] senderComponents = new BaseComponent[]{senderNameComponent, senderMessageComponent};

        target.sendMessage(senderComponents);

        targetAccount.setLastTellSenderUserName(sender.getName());
        senderAccount.setLastTellSenderUserName(target.getName());

        async(targetAccount.getData()::saveData);
        async(senderAccount.getData()::saveData);
    }
}
