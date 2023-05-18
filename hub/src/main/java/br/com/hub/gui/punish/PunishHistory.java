package br.com.hub.gui.punish;

import br.com.core.Core;
import br.com.core.crud.mongo.AccountMongoCRUD;
import br.com.core.data.AccountData;
import br.com.core.data.DuelData;
import br.com.core.data.object.PunishmentDAO;
import br.com.hub.Hub;
import br.com.hub.gui.statistics.PlayerStatisticGUI;
import br.com.hub.gui.statistics.TeamChooseGUI;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PunishHistory implements InventoryProvider {

    private final Set<PunishmentDAO> punishments;
    private final String name;

    private static final String ID = "PUNISH_HISTORY_GUI";
    private static final int MAX_ROWS = 3;
    private static final int MAX_COLUMNS = 9;

    public final SmartInventory INVENTORY;

    public PunishHistory(AccountData data) {
        this.name = data.getName();
        this.punishments = data.getPunishments();
        this.INVENTORY = SmartInventory.builder()
                .id(ID)
                .provider(this)
                .manager(Hub.getInstance().getInventoryManager())
                .size(MAX_ROWS, MAX_COLUMNS)
                .title("Histórico de bans de " + name)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        ClickableItem[] items = new ClickableItem[punishments.size()];

        int i = 0;

        for (PunishmentDAO punishment : punishments) {

            ItemStack itemStack = new ItemStack(Material.PAPER, 1);

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(punishment.getId());

            List<String> lore = new ArrayList<>();

            long expiration = Long.parseLong(punishment.getExpiration());

            String expirationDate = Core.DATE_FORMAT.format(new Date(expiration));

            String whoBannedName = AccountMongoCRUD.get(UUID.fromString(punishment.getWhoBanned())).getName();

            String punishmentDate = Core.DATE_FORMAT.format(new Date(Long.parseLong(punishment.getDate())));

            lore.add(ChatColor.WHITE + "STATUS :" + (expiration > System.currentTimeMillis() ? "BANIDO" : "EXPIRADO"));
            lore.add(ChatColor.WHITE + (expiration > System.currentTimeMillis() ? "expira em: " + expirationDate : "expirou em: " + expirationDate));
            lore.add(ChatColor.WHITE + "id de banimento: " + punishment.getId());
            lore.add(ChatColor.WHITE + "razão: " + punishment.getReason());
            lore.add(ChatColor.WHITE + "quem baniu: " + (whoBannedName == null ? "inexistente" : whoBannedName));
            lore.add(ChatColor.WHITE + "data do banimento: " + punishmentDate);
            lore.add(ChatColor.WHITE + "ip vinculado: " + punishment.getIp());

            itemMeta.setLore(lore);

            itemStack.setItemMeta(itemMeta);

            items[i] = ClickableItem.empty(itemStack);

            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(2, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(2, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY.open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
