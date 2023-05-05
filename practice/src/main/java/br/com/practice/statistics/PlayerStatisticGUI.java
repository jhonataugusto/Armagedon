package br.com.practice.statistics;


import br.com.core.data.DuelData;
import br.com.practice.Practice;
import br.com.practice.util.serializer.SerializerUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerStatisticGUI implements InventoryProvider {

    private UUID uuid;
    private List<UUID> users;
    private static final String ID = "PLAYER_INVENTORY_GUI";
    private static final int MAX_ROWS = 5;
    private static final int MAX_COLUMNS = 9;

    private DuelData data;

    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);

    public SmartInventory INVENTORY;

    public PlayerStatisticGUI(DuelData data, UUID uuid, List<UUID> users) {
        this.data = data;
        this.uuid = uuid;
        this.users = users;


        this.INVENTORY = SmartInventory.builder()
                .id(ID)
                .provider(this)
                .manager(Practice.getInstance().getInventoryManager())
                .size(MAX_ROWS, MAX_COLUMNS)
                .title("Estatística do jogador")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        String key = data.getNameAndUuidKey(uuid.toString());

        if (!data.getInventories().containsKey(key)) {
            return;
        }

        Inventory inventory = SerializerUtils.deserializeInventory(data.getInventories().get(key), null);
        ItemStack[] items = inventory.getContents();

        int row = 0;
        int column = 0;

        for (ItemStack item : items) {

            contents.set(new SlotPos(row, column), ClickableItem.empty(item));
            column++;

            if (column == 9) {
                column = 0;
                row++;
            }
        }

        ItemStack redstoneBlock = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta redstoneMeta = redstoneBlock.getItemMeta();

        redstoneMeta.setDisplayName("Sair da estatística atual");
        redstoneBlock.setItemMeta(redstoneMeta);

        contents.set(lastPos, ClickableItem.of(redstoneBlock, event -> {
            PlayerChooseGUI playerChooseGUI = new PlayerChooseGUI(users, data);
            player.updateInventory();
            playerChooseGUI.INVENTORY.open(player);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}

