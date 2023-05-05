package br.com.hub.gui.challenge;

import br.com.core.enums.game.GameMode;
import br.com.hub.Hub;
import br.com.hub.gui.editor.KitEditorGUI;
import br.com.hub.icons.PracticeIcons;
import br.com.hub.user.User;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class ChallengeModeSelectorGUI implements InventoryProvider {

    private User target;
    public final SmartInventory INVENTORY;

    public ChallengeModeSelectorGUI(User target) {
        this.target = target;
        this.INVENTORY = SmartInventory.builder()
                .id(ID)
                .provider(this)
                .manager(Hub.getInstance().getInventoryManager())
                .size(MAX_ROWS, MAX_COLUMNS)
                .title("Escolha o modo que deseja duelar.")
                .build();
    }

    private static final String ID = "CHALLENGE_MODE_SELECTOR_GUI";
    private static final int MAX_ROWS = 6;
    private static final int MAX_COLUMNS = 9;

    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);


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

                GameMode mode = GameMode.getByName(item.getName());

                new ChallengeMapSelectorGUI(mode, target).getINVENTORY().open(player);
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
