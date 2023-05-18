package br.com.practice.commands;

import br.com.core.account.Account;
import br.com.core.account.enums.rank.Rank;
import br.com.practice.user.User;
import br.com.practice.util.serializer.SerializerUtils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;

import static br.com.core.utils.hastebin.HasteBinUtils.post;
import static br.com.practice.util.scheduler.SchedulerUtils.async;

@CommandAlias("exportinventory|exportinv")
@Description("Exporta o inventário do jogador")
public class ExportInventoryCommand extends BaseCommand {

    @Default
    public void onExportInventory(Player sender) {

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

        async(() -> {
            try {
                String url = post(SerializerUtils.serializeInventory(sender.getInventory()), true);

                TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText("§aInventário exportado com sucesso."));

                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

                sender.sendMessage(textComponent);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

}
