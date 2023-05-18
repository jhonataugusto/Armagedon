package br.com.hub.commands;

import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.data.AccountData;
import br.com.core.data.object.RankDAO;
import br.com.hub.user.User;
import br.com.hub.util.tag.TagUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static br.com.hub.util.scheduler.SchedulerUtils.async;

@CommandAlias("rank|r|cargo")
@Description("Atualiza cargos de jogadores")
public class RankCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onAdd(Player sender, String name, String rankName, String duration) {

        User userSender = User.fetch(sender.getUniqueId());
        Rank rankSender = userSender.getAccount().getRank();

        if (!Rank.getStaffers().contains(rankSender)) {
            return;
        }

        List<Rank> managerialStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.MANAGERIAL);
        List<Rank> executiveStaff = Rank.getRanksByStafferLevel(Rank.StafferLevel.EXECUTIVE);

        if (!managerialStaff.contains(rankSender) && !executiveStaff.contains(rankSender)) {
            return;
        }

        if (!duration.matches("(\\d+)([dhm])") && !duration.equals("-1")) {
            sender.sendMessage(ChatColor.RED + "Formato de duração inválido");
            return;
        }

        Player target = Bukkit.getPlayer(name);

        AccountData accountData;

        if (target == null) {
            accountData = AccountMongoCRUD.get(name);

            if (accountData == null) {
                sender.sendMessage(ChatColor.RED + "O jogador não existe.");
                return;
            }

        } else {
            User user = User.fetch(target.getUniqueId());
            Account account = user.getAccount();
            accountData = account.getData();

            if (target.equals(sender)) {
                return;
            }
        }

        Rank rank = Rank.getByAliases(rankName);

        if (rankSender.isAboveOrEquals(rank)) {
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

        async(() -> {
            accountData.getRanks().clear();

            if (rank.equals(Rank.MEMBER)) {
                accountData.getRanks().add(new RankDAO(rank.getName(), -1));

            } else {
                accountData.getRanks().add(new RankDAO(rank.getName(), expirationTime));
            }

            async(accountData::saveData);
        });

        if (target != null) {
            User user = User.fetch(target.getUniqueId());
            Account account = user.getAccount();
            account.setData(accountData);
        }

        String tag = rank.getColor() + rank.getDisplayName();

        sender.sendMessage(ChatColor.GREEN + "Você atualizou o cargo de " + accountData.getName() + ChatColor.GREEN + " para " + tag + ChatColor.GREEN + " com sucesso.");

        if (target != null) {
            target.sendMessage(ChatColor.GREEN + "Seu cargo foi atualizado para " + tag + ".");
            TagUtil.loadTag(target.getPlayer(), rank);

            if (expirationTime == -1L) {
                target.sendMessage(ChatColor.YELLOW + "Seu cargo nunca expira.");

            } else {
                Duration remainingTime = Duration.ofMillis(expirationTime - System.currentTimeMillis());
                String formattedTime = getTimeRemaining(remainingTime);
                target.sendMessage(ChatColor.YELLOW + "Seu cargo irá expirar em " + formattedTime + ".");
            }
        }
    }

    public static String getTimeRemaining(Duration duration) {
        Instant now = Instant.now();
        Instant expiration = now.plus(duration);
        Duration remaining = Duration.between(now, expiration);
        long days = remaining.toDays();
        long hours = remaining.toHours() % 24;
        long minutes = remaining.toMinutes() % 60;
        return String.format("%d dias, %d horas, %d minutos", days, hours, minutes);
    }


    public static Duration parseDuration(String input) {
        Pattern pattern = Pattern.compile("(\\d+)([dhm])");
        Matcher matcher = pattern.matcher(input);
        long millis = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "d":
                    millis += TimeUnit.DAYS.toMillis(value);
                    break;
                case "h":
                    millis += TimeUnit.HOURS.toMillis(value);
                    break;
                case "m":
                    millis += TimeUnit.MINUTES.toMillis(value);
                    break;
                // adicione outros casos para outras unidades de tempo, se necessário
            }
        }

        return Duration.ofMillis(millis);
    }
}
