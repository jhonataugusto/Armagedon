package br.com.hub.gui.rank;


import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.data.AccountData;
import br.com.hub.Hub;
import br.com.hub.icons.PracticeIcons;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LeaderboardsGUI implements InventoryProvider {
    private static final String ID = "LEADERBOARDS_GUI";

    private static final int MAX_ROWS = 6;
    private static final int MAX_COLUMNS = 9;

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(new LeaderboardsGUI())
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Melhores jogadores")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        int row = 1;
        int column = 1;

        for (PracticeIcons item : PracticeIcons.getValues()) {
            if (item.equals(PracticeIcons.RANKED_MODE) || item.equals(PracticeIcons.UNRANKED_MODE)) {
                continue;
            }

            ItemStack gameModeItem = item.toItemStack();

            ArrayList<String> lore = new ArrayList<>();
            List<AccountData> topAccounts = AccountMongoCRUD.getTopAccounts(10, item.getName());

            lore.add(" ");

            int i = 1;

            for (AccountData account : topAccounts) {
                lore.add(ChatColor.WHITE + "TOP " + i + ". " + account.getName() + ": " + account.getEloByGameModeName(item.getName()).getElo() + " ELO");
                i++;
            }

            lore.add(" ");

            ItemMeta itemMeta = gameModeItem.getItemMeta();
            itemMeta.setLore(lore);
            gameModeItem.setItemMeta(itemMeta);

            SlotPos pos = new SlotPos(row, column);

            contents.set(pos, ClickableItem.empty(gameModeItem));

            item.setRow(row);
            item.setColumn(column);

            column++;

            if (column >= MAX_COLUMNS) {
                column = 1;
                row++;
            }

            if (row >= MAX_ROWS) {
                break;
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
