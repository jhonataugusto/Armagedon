package br.com.hub.gui.preferences;

import br.com.core.account.enums.preferences.Preference;
import br.com.core.data.object.PreferenceDAO;
import br.com.hub.Hub;
import br.com.hub.user.User;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static br.com.hub.util.scheduler.SchedulerUtils.async;

@Getter
@Setter
public class PreferencesGUI implements InventoryProvider {

    private static final String ID = "PREFERENCES_GUI";
    private static final int MAX_ROWS = 3;
    private static final int MAX_COLUMNS = 9;


    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);

    public final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(this)
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Ajuste suas preferencias")
            .build();


    @Override
    public void init(Player player, InventoryContents contents) {

        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[Preference.values().length];

        int i = 0;

        for (Preference preference : Preference.values()) {

            ItemStack itemStack = new ItemStack(Material.PAPER);

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(preference.getDisplayName());

            List<String> lore = new ArrayList<>();

            PreferenceDAO preferenceDAO = user.getAccount().getData().getPreferences().stream().filter(p -> preference.getName().equals(p.getName())).findFirst().orElse(null);

            if (preferenceDAO == null) {
                continue;
            }

            if (preferenceDAO.getType() != null) {
                String[] type = preferenceDAO.getType().split("_");
                String name = type[type.length - 1];
                lore.add(ChatColor.GREEN + name);
            } else {
                lore.add(preferenceDAO.isActive() ? ChatColor.GREEN + "Ativado" : ChatColor.RED + "Desativado");
            }

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(itemStack, event -> {

                if (preferenceDAO.getType() != null) {
                    player.updateInventory();
                    new PreferencesTypeGUI(preference, preferenceDAO, user).getINVENTORY().open(player);
                    return;
                } else {
                    preferenceDAO.setActive(!preferenceDAO.isActive());
                }

                async(() -> {
                    user.getAccount().getData().saveData();
                });

                player.updateInventory();
                this.getINVENTORY().open(player);
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

    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
