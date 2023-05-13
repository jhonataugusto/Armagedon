package br.com.practice.listener;

import br.com.practice.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        if (user.getArena() == null) {
            return;
        }

        event.setCancelled(!user.getArena().getGame().isAllowedBuild());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        if (user.getArena() == null) {
            return;
        }

        event.setCancelled(!user.getArena().getGame().isAllowedBuild());
    }
}
