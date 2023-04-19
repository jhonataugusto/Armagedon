package br.com.armagedon.lobby.practice.listeners;

import br.com.armagedon.items.PracticeItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
}
