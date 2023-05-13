package br.com.hub.lobby.practice.commands.match;

import br.com.core.crud.mongo.DuelContextMongoCRUD;
import br.com.core.data.DuelData;
import br.com.hub.gui.statistics.PlayerStatisticGUI;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;


@CommandAlias("matchsolo")
@Description("Veja as estatísticas de um duelo 1v1 específico")
public class MatchSoloCommand extends BaseCommand {
    @Default
    public static void onMatchSolo(Player sender, @Single String duelUuid, @Single String targetUuid) {
        UUID uuid = UUID.fromString(duelUuid);
        DuelData duelData = DuelContextMongoCRUD.get(uuid);


        if (duelData == null) {
            sender.sendMessage(ChatColor.RED + "O duelo que você está procurando não existe.");
            return;
        }

        PlayerStatisticGUI statisticGUI = new PlayerStatisticGUI(duelData, UUID.fromString(targetUuid), null);
        statisticGUI.getINVENTORY().open(sender.getPlayer());
    }
}
