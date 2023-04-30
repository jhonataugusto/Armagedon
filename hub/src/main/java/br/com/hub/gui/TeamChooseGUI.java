package br.com.hub.gui;

import br.com.core.data.DuelData;
import br.com.hub.Hub;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class TeamChooseGUI implements InventoryProvider {

    private static final String ID = "STATISTICS_GUI";
    private static final int MAX_ROWS = 3;
    private static final int MAX_COLUMNS = 9;

    private DuelData data;

    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);

    public final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(this)
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Escolha um time")
            .build();

    public TeamChooseGUI(DuelData data) {
        this.data = data;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        ItemStack blueBlock = new ItemStack(Material.LAPIS_BLOCK);
        ItemStack redBlock = new ItemStack(Material.REDSTONE_BLOCK);

        contents.set(new SlotPos(1, 3), ClickableItem.of(blueBlock, event -> {
            PlayerChooseGUI playerChooseGUI = new PlayerChooseGUI(data.getTeam1(), data);
            player.updateInventory();
            playerChooseGUI.INVENTORY.open(player);
        }));

        contents.set(new SlotPos(1, 5), ClickableItem.of(redBlock, event -> {
            PlayerChooseGUI playerChooseGUI = new PlayerChooseGUI(data.getTeam2(), data);
            player.updateInventory();
            playerChooseGUI.INVENTORY.open(player);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
