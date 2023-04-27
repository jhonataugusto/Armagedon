package br.com.hub.gui;

import br.com.core.data.DuelContextData;
import br.com.hub.Hub;
import br.com.hub.util.mojang.MojangAPI;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@Setter
public class PlayerChooseGUI implements InventoryProvider {

    private List<UUID> users;
    private static final String ID = "STATISTICS_GUI";
    private static final int MAX_ROWS = 3;
    private static final int MAX_COLUMNS = 9;

    private DuelContextData data;

    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);

    public final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(this)
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Escolha um jogador")
            .build();

    public PlayerChooseGUI(List<UUID> users, DuelContextData data) {
        this.users = users;
        this.data = data;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        ClickableItem[] items = new ClickableItem[users.size()];

        Map<UUID, ItemStack> userItemStackMap = new HashMap<>();

        for (UUID userUuid : users) {


            String[] userNameAndUuid = data.getNameAndUuidKey(userUuid.toString()).split("_");

            if (userNameAndUuid.length == 0) {
                return;
            }

            String userName = userNameAndUuid[0];


            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);

            profile.getProperties().put("textures", new Property("textures", new String(Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", MojangAPI.getSkinUrl(userName)).getBytes()))));

            try {
                Field f = meta.getClass().getDeclaredField("profile");
                f.setAccessible(true);
                f.set(meta, profile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            itemStack.getItemMeta().setDisplayName(userName);

            itemStack.setItemMeta(meta);

            userItemStackMap.put(userUuid, itemStack);
        }

        int i = 0;
        for (UUID userUuid : userItemStackMap.keySet()) {
            ItemStack itemStack = userItemStackMap.get(userUuid);
            items[i] = ClickableItem.of(itemStack, event -> {
                PlayerStatisticGUI playerStatisticGUI = new PlayerStatisticGUI(data, userUuid, users);
                player.updateInventory();
                playerStatisticGUI.INVENTORY.open(player);
            });
            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(2, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(2, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY.open(player, pagination.next().getPage())));


        ItemStack redstoneBlock = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta redstoneMeta = redstoneBlock.getItemMeta();

        redstoneMeta.setDisplayName("Sair do time atual");
        redstoneBlock.setItemMeta(redstoneMeta);

        contents.set(lastPos, ClickableItem.of(redstoneBlock, event -> {
            TeamChooseGUI teamChooseGUI = new TeamChooseGUI(data);
            player.updateInventory();
            teamChooseGUI.INVENTORY.open(player);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
