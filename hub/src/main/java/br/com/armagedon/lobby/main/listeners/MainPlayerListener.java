package br.com.armagedon.lobby.main.listeners;

import br.com.armagedon.gui.ServerGUI;
import br.com.armagedon.items.LobbyItems;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class MainPlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }

        NBT.modify(event.getItem(), nbt -> {
            if (nbt.hasCustomNbtData()) {

                if (nbt.getString(LobbyItems.SERVER_LIST.getKey()).equals(LobbyItems.SERVER_LIST.getValue())) {
                    ServerGUI.INVENTORY.open(event.getPlayer());
                }
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (LobbyItems items : LobbyItems.values()) {
            ItemStack itemStack = items.toItemStack();
            int position = items.getPosition();
            event.getPlayer().getInventory().setItem(position, itemStack);
        }
    }
}
