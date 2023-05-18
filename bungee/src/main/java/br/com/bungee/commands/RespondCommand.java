package br.com.bungee.commands;

import br.com.bungee.Bungee;
import br.com.core.account.Account;
import br.com.core.account.enums.preferences.Preference;
import br.com.core.data.object.PreferenceDAO;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@CommandAlias("respond|r")
@Description("responde o /tell de alguém")
public class RespondCommand extends BaseCommand {

    @Default
    public void onRespond(ProxiedPlayer responder, String[] message) {

        Account responderAccount = Bungee.getInstance().getAccountStorage().getAccount(responder.getUniqueId());

        PreferenceDAO preference = responderAccount.getData().getPreferenceByName(Preference.DISABLE_PRIVATE_MESSAGES);

        if (preference.isActive()) {
            responder.sendMessage(ChatColor.RED + "Você não pode enviar mensagens a ninguém.");
            return;
        }

        if (responderAccount.getLastTellSenderUserName() == null) {
            responder.sendMessage(ChatColor.RED + "Ninguém mandou mensagem pra você.");
            return;
        }

        if (message == null || message.length == 0) {
            responder.sendMessage(ChatColor.RED + "A mensagem não pode ser vazia.");
            return;
        }

        ProxiedPlayer sender = Bungee.getInstance().getProxy().getPlayer(responderAccount.getLastTellSenderUserName());

        if (sender == null) {
            responder.sendMessage(ChatColor.RED + "Esse jogador não existe.");
            return;
        }

        Account senderAccount = Account.fetch(sender.getUniqueId());

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < message.length; i++) {

            if (i == message.length - 1) {
                builder.append(message[i]);
            } else {
                builder.append(message[i]).append(" ");
            }
        }

        TextComponent responderNameComponent = new TextComponent(ChatColor.GRAY + "(Para " + senderAccount.getRank().getColor() + senderAccount.getName() + ChatColor.GRAY + ") ");
        TextComponent responderMessageComponent = new TextComponent(ChatColor.GRAY + builder.toString());
        BaseComponent[] responderComponents = new BaseComponent[]{responderNameComponent, responderMessageComponent};

        responder.sendMessage(responderComponents);

        TextComponent senderNameComponent = new TextComponent(ChatColor.GRAY + "(De " + responderAccount.getRank().getColor() + responderAccount.getName() + ChatColor.GRAY + ") ");
        TextComponent senderMessageComponent = new TextComponent(ChatColor.GRAY + builder.toString());
        BaseComponent[] senderComponents = new BaseComponent[]{senderNameComponent, senderMessageComponent};

        sender.sendMessage(senderComponents);

        senderAccount.setLastTellSenderUserName(responderAccount.getName());
    }
}
