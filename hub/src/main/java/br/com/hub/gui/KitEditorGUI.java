package br.com.hub.gui;

import br.com.core.enums.game.GameMode;
import br.com.hub.user.User;
import br.com.hub.util.serializer.SerializerUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static br.com.hub.util.scheduler.SchedulerUtils.async;

public class KitEditorGUI implements Listener {


    @Getter
    private static final KitEditorGUI instance = new KitEditorGUI();

    private final String id = "editor";
    private Inventory kitEditorInventory;
    private Player player;
    protected final int MAXIMUM_INVENTORY_EDITOR_SLOT = 44;

    private final Map<Player, GameMode> editMode = new HashMap<>();


    //TODO: terminar isso aqui mais tarde!

    public void open(Player player, GameMode gameMode) {
        this.player = player;

        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        Inventory defaultInventory = SerializerUtils.deserializeInventory(gameMode.getDefaultInventoryEncoded(), player);
        Inventory customInventory = SerializerUtils.deserializeInventory(user.getAccount().getData().getInventories().get(gameMode.getName()), player);
        Inventory kitEditorInventory = Bukkit.createInventory(player, InventoryType.PLAYER, id);

        if(defaultInventory == null) {
            return;
        }

        if (customInventory == null) {
            kitEditorInventory.setContents(defaultInventory.getContents());
        } else {
            kitEditorInventory.setContents(customInventory.getContents());
        }
        editMode.put(player, gameMode);
        player.openInventory(kitEditorInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if (event.getInventory() == null) {
            return;
        }

        if (!event.getInventory().getName().contains(id)) {
            return;
        }

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        if (event.getClick().equals(ClickType.SHIFT_RIGHT) || event.getClick().equals(ClickType.SHIFT_LEFT)) {
            event.setCancelled(true);
            return;
        }

        if (event.getWhoClicked().getItemOnCursor() != null && !player.getItemOnCursor().getType().equals(Material.AIR) && event.getCurrentItem().getType().equals(Material.AIR) && event.getSlot() >= MAXIMUM_INVENTORY_EDITOR_SLOT) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getInventory().getName().contains(id)) {
            return;
        }

        if (event.getInventory() == null) {
            return;
        }

        if (event.getRawSlots().stream().anyMatch(slot -> slot > MAXIMUM_INVENTORY_EDITOR_SLOT)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {

        if (event.getSource() == null || event.getDestination() == null) {
            return;
        }

        if (!event.getDestination().getName().equalsIgnoreCase(id) || !event.getSource().getName().equalsIgnoreCase(id)) {
            return;
        }

        Inventory source = event.getSource(), destination = event.getDestination();

        if (source.getName().contains(id) && !destination.getName().contains(id)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        User user = User.fetch(player.getUniqueId());

        if (!event.getInventory().getName().contains(id)) {
            return;
        }

        if (player.getItemOnCursor() != null) {

            event.getInventory().addItem(player.getItemOnCursor());

            player.setItemOnCursor(new ItemStack(Material.AIR));
        }

        async(() -> {
            String inventorySerialized = SerializerUtils.serializeInventory(event.getInventory());

            user.getAccount().getData().getInventories().put(editMode.get(player).getName(), inventorySerialized);
            user.getAccount().getData().saveData();
            editMode.remove(player);

            player.sendMessage(ChatColor.GREEN + "Inventário salvo com sucesso");
        });
    }

}
