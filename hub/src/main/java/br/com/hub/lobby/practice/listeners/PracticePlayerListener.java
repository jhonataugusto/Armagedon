package br.com.hub.lobby.practice.listeners;

import br.com.hub.gui.KitEditorGUI;
import br.com.hub.gui.LeaderboardsGUI;
import br.com.hub.gui.ModeSelectorGUI;
import br.com.hub.gui.ServerGUI;
import br.com.hub.items.LobbyItems;
import br.com.hub.items.PracticeItems;
import br.com.hub.user.User;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PracticePlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();

        for (PracticeItems item : PracticeItems.values()) {
            player.getInventory().setItem(item.getPosition(), item.toItemStack());
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }

        NBT.modify(event.getItem(), nbt -> {
            if (nbt.hasCustomNbtData()) {

                if (nbt.getString(PracticeItems.KIT_EDITOR.getKEY()).equals(PracticeItems.KIT_EDITOR.getValue())) {
                    ModeSelectorGUI.INVENTORY.open(event.getPlayer());
                }

                if (nbt.getString(PracticeItems.LEADERBOARD.getKEY()).equals(PracticeItems.LEADERBOARD.getValue())) {
                    LeaderboardsGUI.INVENTORY.open(event.getPlayer());
                }
            }
        });
    }
}
