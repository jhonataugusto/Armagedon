package br.com.armagedon.lobby.practice.listeners;

import br.com.armagedon.items.PracticeItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PracticePlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (PracticeItems items : PracticeItems.values()) {
            event.getPlayer().getInventory().setItem(items.getPosition(), items.toItemStack());
        }
    }
}
