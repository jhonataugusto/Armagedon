package br.com.bungee.commands;


import br.com.bungee.Bungee;
import br.com.core.Core;
import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.data.AccountData;
import br.com.core.data.object.PunishmentDAO;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.Duration;
import java.util.Date;

import static br.com.bungee.util.scheduler.SchedulerUtils.async;
import static br.com.core.utils.timings.TimingUtils.parseDuration;

@CommandAlias("punish|p|ban|banir|punir")
public class PunishCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onAdd(ProxiedPlayer sender, String name, String duration, String reason) {

        Account senderAccount = Account.fetch(sender.getUniqueId());
        Rank rankSender = senderAccount.getRank();

        if (!Rank.getStaffers().contains(rankSender)) {
            return;
        }

        if (!duration.matches("(\\d+)([dhm])") && !duration.equals("-1")) {
            sender.sendMessage(ChatColor.RED + "Formato de duração inválido");
            return;
        }

        AccountData targetAccount = AccountMongoCRUD.get(name);

        if (targetAccount == null) {
            sender.sendMessage(ChatColor.RED + "O jogador não existe.");
            return;
        }

        if (targetAccount.equals(senderAccount.getData())) {
            sender.sendMessage(ChatColor.RED + "O jogador não pode ser você.");
            return;
        }

        PunishmentDAO punishmentActive = targetAccount.getPunishments().stream().filter(PunishmentDAO::isActive).findFirst().orElse(null);
        boolean hasPunishmentActive = punishmentActive != null;

        if (hasPunishmentActive) {
            sender.sendMessage(ChatColor.RED + "Este jogador já está banido.");
            return;
        }

        long expirationTime;

        if (duration.equalsIgnoreCase("-1")) {
            expirationTime = -1L;
        } else {
            Duration durationParse = parseDuration(duration);
            long durationMillis = durationParse.toMillis();
            expirationTime = System.currentTimeMillis() + durationMillis;
        }

        //TODO: vincular uma conta com um IP!
        PunishmentDAO punishment = new PunishmentDAO(senderAccount.getUuid().toString(), targetAccount.getUuid().toString(), null, String.valueOf(System.currentTimeMillis()),
                reason, String.valueOf(expirationTime), true);

        targetAccount.getPunishments().add(punishment);
        async(targetAccount::saveData);

        ProxiedPlayer target = Bungee.getInstance().getProxy().getPlayer(name);

        if (target != null) {

            TextComponent headerComponent = new TextComponent(ChatColor.RED + "\n§lVocê foi banido\n\n".toUpperCase());
            TextComponent idComponent = new TextComponent(ChatColor.YELLOW + "Banimento: " + punishment.getId() + "\n");
            TextComponent punishDurationComponent = new TextComponent(ChatColor.YELLOW + "Data de expiração: §r" + ChatColor.RED + (expirationTime > 0 ? Core.DATE_FORMAT.format(new Date(expirationTime)) : "PERMANENTE") + "\n");
            TextComponent reasonComponent = new TextComponent(ChatColor.YELLOW + "Razão: §r" + punishment.getReason() + "\n\n");
            TextComponent appealComponent = new TextComponent(ChatColor.AQUA + "Foi banido incorretamente? peça appeal em nosso discord: §r " + "§ldiscord.io/armaggedon".toUpperCase());

            BaseComponent[] message = new BaseComponent[]{headerComponent, idComponent, punishDurationComponent, reasonComponent, appealComponent};

            target.disconnect(message);

            sender.sendMessage(ChatColor.GREEN + "você baniu " + targetAccount.getName() + " com sucesso.");
        }
    }

}
