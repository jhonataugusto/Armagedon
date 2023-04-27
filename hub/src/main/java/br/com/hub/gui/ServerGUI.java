package br.com.hub.gui;

import br.com.hub.Hub;
import br.com.core.crud.redis.ServerRedisCRUD;
import br.com.core.data.ServerData;
import br.com.core.enums.server.Server;
import br.com.hub.icons.ServerIcons;
import br.com.hub.util.bungee.BungeeUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static br.com.hub.util.scheduler.SchedulerUtils.sync;

import java.util.*;

@Getter
public class ServerGUI implements InventoryProvider {

    @Setter
    private Map<ServerIcons, ServerData> serversLoaded = new HashMap<>();
    private static final String ID = "SERVER_LIST_GUI";

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(new ServerGUI())
            .manager(Hub.getInstance().getInventoryManager())
            .size(3, 9)
            .title("Lista de Servidores")
            .build();

    private final Random random = new Random();

    @Override
    public void init(Player player, InventoryContents contents) {

        int row = 1;
        int column = 1;
        final int MAX_COLUMNS = 9;
        final int MAX_ROWS = 3;


        for (ServerIcons item : ServerIcons.getValues()) {

            ServerData data = ServerRedisCRUD.findByName(item.getName());

            if (data == null || !(item.isShown())) {
                continue;
            }

            boolean online = data.isOnline();

            Material material = online ? item.getOnlineMaterial() : item.getOfflineMaterial();

            SlotPos pos = new SlotPos(row, column);

            contents.set(pos, ClickableItem.of(item.toItemStack(material), event -> {

                Server server = Server.getByName(item.getName());

                if (online) {
                    BungeeUtils.connect((Player) event.getWhoClicked(), server);
                } else {
                    event.getWhoClicked().sendMessage(ChatColor.RED + "Este servidor está desligado.");
                }

            }));

            item.setRow(row);
            item.setColumn(column);

            serversLoaded.put(item, data);

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

        for (Map.Entry<ServerIcons, ServerData> entry : serversLoaded.entrySet()) {

            ServerIcons item = entry.getKey();
            ServerData data = entry.getValue();

            if (!(item.isShown())) {
                continue;
            }

            boolean online = data.isOnline();

            Material material = online ? item.getOnlineMaterial() : item.getOfflineMaterial();

            SlotPos pos = new SlotPos(item.getRow(), item.getColumn());

            contents.set(pos, ClickableItem.of(item.toItemStack(material), event -> {

                Server server = Server.getByName(item.getName());

                if (online) {
                    BungeeUtils.connect((Player) event.getWhoClicked(), server);
                } else {
                    event.getWhoClicked().sendMessage(ChatColor.RED + "Este servidor está desligado.");
                }

            }));
        }

        updateServerIcons();
    }

    public void updateServerIcons() {

        for (ServerIcons item : ServerIcons.values()) {
            ServerData data = ServerRedisCRUD.findByName(item.getName());

            if (data == null) {
                return;
            }

            serversLoaded.putIfAbsent(item, data);

            boolean dataChanged = !serversLoaded.get(item).equals(data);

            if (dataChanged) {
                serversLoaded.replace(item, data);
            }
        }
    }
}



