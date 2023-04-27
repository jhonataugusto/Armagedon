package br.com.hub.lobby.practice.commands;

import br.com.core.crud.mongo.DuelContextMongoCRUD;
import br.com.core.data.DuelContextData;
import br.com.hub.gui.TeamChooseGUI;
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
    public void onMatch(Player sender, @Single String uuidString) {

        UUID uuid = UUID.fromString(uuidString);

        DuelContextData duelContextData = DuelContextMongoCRUD.get(uuid);

        if(duelContextData == null) {
            sender.sendMessage(ChatColor.RED + "O duelo que você está procurando não existe.");
            return;
        }

        TeamChooseGUI teamChooseGUI = new TeamChooseGUI(duelContextData);
        teamChooseGUI.INVENTORY.open(sender.getPlayer());
    }
}
