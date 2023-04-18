package br.com.armagedon.gui;

import br.com.armagedon.Hub;
import br.com.armagedon.icons.PracticeIcons;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;

import org.bukkit.entity.Player;

public class PracticeGUI implements InventoryProvider {
    private static final String ID = "PRACTICE_GAMES_GUI";

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(new PracticeGUI())
            .manager(Hub.getInstance().getInventoryManager())
            .size(6, 9)
            .title("Modos de jogo")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        int row = 1;
        int column = 1;
        final int MAX_ROWS = 6;
        final int MAX_COLUMNS = 9;


        for (PracticeIcons item : PracticeIcons.getValues()) {

            if (item.equals(PracticeIcons.RANKED_SWITCHER)) {
                continue;
            }

            SlotPos pos = new SlotPos(row, column);

            contents.set(pos, ClickableItem.of(item.toItemStack(), event -> {
                //TODO: entrar na fila de duelo...
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

        SlotPos pos = new SlotPos(MAX_ROWS, MAX_COLUMNS);

        contents.set(pos, ClickableItem.of(PracticeIcons.RANKED_SWITCHER.toItemStack(), event -> {
            //TODO: entrar na fila de duelo...
            event.getWhoClicked().sendMessage("Trocou de configuração (Modo ranqueado habilitado)?");
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        contents.set(6,9,ClickableItem.of(PracticeIcons.RANKED_SWITCHER.toItemStack(), event -> {
            //TODO: atualizar modo ranqueado
            event.getWhoClicked().sendMessage("Atualizado?");
        }));
    }
}
