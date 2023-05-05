package br.com.practice.commands;

import br.com.core.crud.mongo.DuelContextMongoCRUD;
import br.com.core.data.DuelData;
import br.com.practice.gui.statistics.TeamChooseGUI;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("match")
@Description("Veja as estatísticas de um duelo específico")
public class MatchCommand extends BaseCommand {

    @Default
    public void onMatch(Player sender, @Single String duelUuid) {

        UUID uuid = UUID.fromString(duelUuid);

        DuelData duelData = DuelContextMongoCRUD.get(uuid);


        if (duelData == null) {
            sender.sendMessage(ChatColor.RED + "O duelo que você está procurando não existe.");
            return;
        }

        TeamChooseGUI teamChooseGUI = new TeamChooseGUI(duelData);
        teamChooseGUI.INVENTORY.open(sender.getPlayer());
    }
}
