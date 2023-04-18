package br.com.armagedon.listeners;

import br.com.armagedon.gui.ServerGUI;
import br.com.armagedon.util.bungee.BungeeUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
       // event.setCancelled(true);
        //TODO: fazer as properties dessa conta também
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //event.setCancelled(true);
        BungeeUtils.connect(event.getPlayer(), "test");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        //ServerGUI.INVENTORY.open(event.getPlayer());
    }

    @EventHandler
    public void onBlockBurnEvent(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDispenseEvent(BlockDispenseEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplodeEvent(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFormEvent(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpreadEvent(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecayEvent(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }
}
