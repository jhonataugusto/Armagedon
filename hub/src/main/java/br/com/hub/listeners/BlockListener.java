package br.com.hub.listeners;

import br.com.hub.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        event.setCancelled(!user.isBuilding());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        event.setCancelled(!user.isBuilding());
    }
}
