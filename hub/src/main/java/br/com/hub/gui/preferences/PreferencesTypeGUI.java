package br.com.hub.gui.preferences;

import br.com.core.account.enums.preferences.Preference;
import br.com.core.account.enums.preferences.type.PreferenceType;
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

import java.util.ArrayList;
import java.util.List;

import static br.com.hub.util.scheduler.SchedulerUtils.async;

@Getter
@Setter
public class PreferencesTypeGUI implements InventoryProvider {

    private final Preference preference;
    private final User user;
    private final PreferenceDAO preferenceDAO;

    private static final String ID = "PREFERENCES_TYPE_GUI";
    private static final int MAX_ROWS = 3;
    private static final int MAX_COLUMNS = 9;


    SlotPos lastPos = new SlotPos(MAX_ROWS - 1, MAX_COLUMNS - 1);

    public final SmartInventory INVENTORY = SmartInventory.builder()
            .id(ID)
            .provider(this)
            .manager(Hub.getInstance().getInventoryManager())
            .size(MAX_ROWS, MAX_COLUMNS)
            .title("Escolha seu tipo")
            .build();

    public PreferencesTypeGUI(Preference preference, PreferenceDAO preferenceDAO, User user) {
        this.preference = preference;
        this.user = user;
        this.preferenceDAO = preferenceDAO;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[Preference.values().length];

        int i = 0;


        //se o nome da preferencia for igual as preferencias, coloque aqui.

        for (PreferenceType preferenceType : PreferenceType.getTypesFromPreference(preference)) {

            ItemStack itemStack = new ItemStack(Material.PAPER);

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(preferenceType.getDisplayName());
            List<String> lore = new ArrayList<>();

            String[] type = preferenceType.name().split("_");
            String name = type[type.length - 1];

            if (preferenceType.name().equalsIgnoreCase(preferenceDAO.getType())) {
                lore.add(ChatColor.GREEN + name);
            } else {
                lore.add(name);
            }

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(itemStack, event -> {

                user.getAccount().getData().getPreferences().forEach(prefDAO -> {
                    if (prefDAO.getName().equalsIgnoreCase(preferenceDAO.getName())) {
                        prefDAO.setType(preferenceType.name().toLowerCase());
                    }

                    async(user.getAccount().getData()::saveData);

                    player.updateInventory();
                    this.getINVENTORY().open(player);
                });
            });

            i++;

            pagination.setItems(items);
            pagination.setItemsPerPage(7);

            pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

            contents.set(2, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                    e -> INVENTORY.open(player, pagination.previous().getPage())));
            contents.set(2, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                    e -> INVENTORY.open(player, pagination.next().getPage())));


            ItemStack redstoneBlock = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta redstoneMeta = redstoneBlock.getItemMeta();

            redstoneMeta.setDisplayName("Voltar ao menu principal");
            redstoneBlock.setItemMeta(redstoneMeta);

            contents.set(lastPos, ClickableItem.of(redstoneBlock, event -> {
                player.updateInventory();
                new PreferencesGUI().getINVENTORY().open(player);
            }));

        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}

