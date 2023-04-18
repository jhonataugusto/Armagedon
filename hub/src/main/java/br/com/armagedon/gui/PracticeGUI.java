package br.com.armagedon.gui;

import br.com.armagedon.Hub;
import br.com.armagedon.icons.PracticeIcons;
import br.com.armagedon.lobby.practice.Practice;
import br.com.armagedon.lobby.practice.queue.properties.QueueProperties;
import br.com.armagedon.user.User;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PracticeGUI implements InventoryProvider {
    private static final String ID = "PRACTICE_GAMES_GUI";

    private static final int MAX_ROWS = 6;
    private static final int MAX_COLUMNS = 9;

    SlotPos lastPos = new SlotPos(MAX_ROWS, MAX_COLUMNS);

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(new PracticeGUI())
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Modos de jogo")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        boolean ranked = contents.property("ranked", false);
        int row = 1;
        int column = 1;


        for (PracticeIcons item : PracticeIcons.getValues()) {

            if (item.equals(PracticeIcons.UNRANKED_MODE)) {
                continue;
            }

            SlotPos pos = new SlotPos(row, column);

            contents.set(pos, ClickableItem.of(item.toItemStack(), event -> {

                Practice instance = (Practice) Hub.getInstance().getLobby();

                User user = User.fetch(event.getWhoClicked().getUniqueId());
                boolean inQueue = instance.getQueue().inQueue(user);

                if (!inQueue) {
                    instance.getQueue().enter(user, new QueueProperties(item.getName(), contents.property("ranked")));
                }

                event.getWhoClicked().sendMessage("Entrou na fila de duelo.");
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

        //TODO: fazer o usuário mudar de ranqueada ao abrir o inventário nas configurações: "on_open_inventory_mode_default_ranked_or_unranked" algo assim
        if (ranked) {
            contents.set(lastPos, ClickableItem.of(PracticeIcons.RANKED_MODE.toItemStack(), event1 -> contents.setProperty("ranked", false)));
        } else {
            contents.set(lastPos, ClickableItem.of(PracticeIcons.UNRANKED_MODE.toItemStack(), event2 -> contents.setProperty("ranked", true)));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        boolean ranked = contents.property("ranked");

        contents.set(lastPos, ClickableItem.of(PracticeIcons.UNRANKED_MODE.toItemStack(), event -> {

            if (ranked) {
                contents.set(lastPos, ClickableItem.of(PracticeIcons.RANKED_MODE.toItemStack(), event2 -> {
                    contents.setProperty("ranked", false);
                }));
            } else {
                contents.set(lastPos, ClickableItem.of(PracticeIcons.UNRANKED_MODE.toItemStack(), event2 -> {
                    contents.setProperty("ranked", true);
                }));
            }
        }));
    }
}
