package br.com.armagedon.lobby.main.listeners;

import br.com.armagedon.gui.ServerGUI;
import br.com.armagedon.items.LobbyItems;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

        Player player = event.getPlayer();

        player.getInventory().clear();

        for (LobbyItems item : LobbyItems.values()) {
            event.getPlayer().getInventory().setItem(item.getPosition(), item.toItemStack());
        }
    }
}
