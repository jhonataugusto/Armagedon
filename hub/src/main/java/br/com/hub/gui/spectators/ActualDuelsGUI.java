package br.com.hub.gui.spectators;

import br.com.core.crud.redis.AccountRedisCRUD;
import br.com.core.crud.redis.DuelRedisCRUD;
import br.com.core.data.AccountData;
import br.com.core.data.DuelData;
import br.com.hub.Hub;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter
@Setter
public class ActualDuelsGUI implements InventoryProvider {

    @Getter
    public static final ActualDuelsGUI instance = new ActualDuelsGUI();
    private static final String ID = "SPECTATORS_GUI";
    private static final int MAX_ROWS = 6;
    private static final int MAX_COLUMNS = 9;

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(instance)
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Duelos atuais")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        List<DuelData> duels = DuelRedisCRUD.getDuels();

        Pagination pagination = contents.pagination();

        ClickableItem[] items = new ClickableItem[duels.size()];

        int i = 0;

        for (DuelData duel : duels) {

            ItemStack itemStack = new ItemStack(duel.isRanked() ? Material.DIAMOND_SWORD : Material.IRON_SWORD, 1);

            ItemMeta itemMeta = itemStack.getItemMeta();

            if (duel.is1v1()) {

                UUID account1Uuid = duel.getTeam1().stream().findFirst().get();
                AccountData account1 = AccountRedisCRUD.findByUuid(account1Uuid);

                UUID account2Uuid = duel.getTeam2().stream().findFirst().get();
                AccountData account2 = AccountRedisCRUD.findByUuid(account2Uuid);

                if (account1 == null || account2 == null) {
                    return;
                }

                itemMeta.setDisplayName(account1.getName() + " VS " + account2.getName());


                List<String> lore = new ArrayList<>();

                lore.add("arena: " + duel.getArenaId());
                lore.add("ranked: " + (duel.isRanked() ? "sim" : "não"));
                lore.add("modo de jogo: " + duel.getGameModeName() + " " + duel.getMapName());

                itemMeta.setLore(lore);

            } else {
                itemMeta.setDisplayName("Luta em equipe");
                List<String> lore = new ArrayList<>();

                lore.add("arena: " + duel.getArenaId());
                lore.add("modo de jogo: " + duel.getGameModeName() + " " + duel.getMapName());
                lore.add("jogadores: ");
                lore.add("   time 1:" + "§r " + duel.getTeam1() + " jogadores");
                lore.add("   time 2:" + "§r " + duel.getTeam2() + " jogadores");

                itemMeta.setLore(lore);
            }

            AccountData randomDuelAccount = AccountRedisCRUD.findByUuid(duel.getTeam1().stream().findFirst().get());

            if (randomDuelAccount == null) {
                return;
            }

            itemStack.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(itemStack, event -> {
                player.performCommand("/spec " + randomDuelAccount.getName());
            });

            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(45);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(5, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY.open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        int state = contents.property("state", 0);

        contents.setProperty("state", state + 1);

        if (state % 5 != 0) return;

        List<DuelData> duels = DuelRedisCRUD.getDuels();

        Pagination pagination = contents.pagination();

        ClickableItem[] items = new ClickableItem[duels.size()];

        int i = 0;

        for (DuelData duel : duels) {

            ItemStack itemStack = new ItemStack(duel.isRanked() ? Material.DIAMOND_SWORD : Material.IRON_SWORD, 1);

            ItemMeta itemMeta = itemStack.getItemMeta();

            if (duel.is1v1()) {

                UUID account1Uuid = duel.getTeam1().stream().findFirst().get();
                AccountData account1 = AccountRedisCRUD.findByUuid(account1Uuid);

                UUID account2Uuid = duel.getTeam2().stream().findFirst().get();
                AccountData account2 = AccountRedisCRUD.findByUuid(account2Uuid);

                if (account1 == null || account2 == null) {
                    return;
                }

                itemMeta.setDisplayName(account1.getName() + " VS " + account2.getName());


                List<String> lore = new ArrayList<>();

                lore.add(ChatColor.WHITE + "arena: " + duel.getArenaId());
                lore.add(ChatColor.WHITE + "ranked: " + (duel.isRanked() ? "sim" : "não"));
                lore.add(ChatColor.WHITE + "modo de jogo: " + duel.getGameModeName() + " " + duel.getMapName());

                itemMeta.setLore(lore);

            } else {
                itemMeta.setDisplayName("Luta em equipe");
                List<String> lore = new ArrayList<>();

                lore.add(ChatColor.WHITE + "arena: " + duel.getArenaId());
                lore.add(ChatColor.WHITE + "modo de jogo: " + duel.getGameModeName() + " " + duel.getMapName());
                lore.add(ChatColor.WHITE + "jogadores: ");
                lore.add(ChatColor.WHITE + "   time 1:" + "§r " + duel.getTeam1() + " jogadores");
                lore.add(ChatColor.WHITE + "   time 2:" + "§r " + duel.getTeam2() + " jogadores");

                itemMeta.setLore(lore);
            }

            AccountData randomDuelAccount = AccountRedisCRUD.findByUuid(duel.getTeam1().stream().findFirst().get());

            if (randomDuelAccount == null) {
                return;
            }

            itemStack.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(itemStack, event -> {
                Bukkit.getServer().dispatchCommand(player, "spec " + randomDuelAccount.getName());
            });

            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(45);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        contents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(5, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY.open(player, pagination.next().getPage())));
    }
}
