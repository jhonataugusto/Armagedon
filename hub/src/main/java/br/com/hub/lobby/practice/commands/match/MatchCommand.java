package br.com.hub.lobby.practice.commands.match;

import br.com.core.crud.mongo.DuelMongoCRUD;
import br.com.core.data.DuelData;
import br.com.hub.gui.statistics.TeamChooseGUI;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("match")
@Description("Veja as estatísticas de um duelo específico")
public class MatchCommand extends BaseCommand {

    @Default
    public void onMatch(Player sender, @Single String duelUuid) {

        UUID uuid = UUID.fromString(duelUuid);

        DuelData duelData = DuelMongoCRUD.get(uuid);


        if (duelData == null) {
            sender.sendMessage(ChatColor.RED + "O duelo que você está procurando não existe.");
            return;
        }

        TeamChooseGUI teamChooseGUI = new TeamChooseGUI(duelData);
        teamChooseGUI.INVENTORY.open(sender.getPlayer());
    }
}
