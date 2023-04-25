package br.com.hub.gui;

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
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PracticeGUI implements InventoryProvider {
    private static final String ID = "PRACTICE_GAMES_GUI";

    private static final int MAX_ROWS = 6;
    private static final int MAX_COLUMNS = 9;

    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);

    private final Map<Player, Boolean> rankedMap = new HashMap<>();

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(new PracticeGUI())
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Modos de jogo")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        //TODO: aqui carregar as preferencias do jogador!
        getRankedMap().put(player, false);

        boolean ranked = getRankedMap().get(player);

        int row = 1;
        int column = 1;


        for (PracticeIcons item : PracticeIcons.getValues()) {


            if (item.equals(PracticeIcons.RANKED_MODE) || item.equals(PracticeIcons.UNRANKED_MODE)) {
                continue;
            }

            SlotPos pos = new SlotPos(row, column);

            contents.set(pos, ClickableItem.of(item.toItemStack(), event -> {

                Practice instance = (Practice) Hub.getInstance().getLobby();

                User user = User.fetch(event.getWhoClicked().getUniqueId());
                boolean inQueue = instance.getQueue().inQueue(user);

                if (!inQueue) {
                    instance.getQueue().enter(user, new QueueProperties(item.getName(), getRankedMap().get(player)));

                    getRankedMap().remove(player);

                    player.closeInventory();
                }

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

        if (ranked) {
            contents.set(lastPos, ClickableItem.of(PracticeIcons.RANKED_MODE.toItemStack(), event1 -> getRankedMap().put(player, false)));
        } else {
            contents.set(lastPos, ClickableItem.of(PracticeIcons.UNRANKED_MODE.toItemStack(), event2 -> getRankedMap().put(player, true)));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        boolean ranked = rankedMap.get(player);

        if (ranked) {
            contents.set(lastPos, ClickableItem.of(PracticeIcons.RANKED_MODE.toItemStack(), event2 -> {
                getRankedMap().put(player, false);
            }));
        } else {
            contents.set(lastPos, ClickableItem.of(PracticeIcons.UNRANKED_MODE.toItemStack(), event2 -> {
                getRankedMap().put(player, true);
            }));
        }


    }
}
