package br.com.hub.gui;

import br.com.core.enums.game.GameMode;
import br.com.hub.Hub;
import br.com.hub.icons.PracticeIcons;
import br.com.hub.lobby.practice.Practice;
import br.com.hub.lobby.practice.queue.properties.QueueProperties;
import br.com.hub.user.User;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ModeSelectorGUI implements InventoryProvider {

    private static final String ID = "MODE_SELECTOR_GUI";
    private static final int MAX_ROWS = 6;
    private static final int MAX_COLUMNS = 9;

    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);


    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(new ModeSelectorGUI())
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Escolha o Kit que deseja editar")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        int row = 1;
        int column = 1;

        for (PracticeIcons item : PracticeIcons.getValues()) {


            if (item.equals(PracticeIcons.RANKED_MODE) || item.equals(PracticeIcons.UNRANKED_MODE)) {
                continue;
            }

            SlotPos pos = new SlotPos(row, column);

            contents.set(pos, ClickableItem.of(item.toItemStack(), event -> {
                KitEditorGUI.getInstance().open(player.getPlayer(), GameMode.getByName(item.getName()));
            }));

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
