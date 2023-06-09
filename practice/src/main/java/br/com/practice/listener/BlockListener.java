package br.com.practice.listener;

import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.arena.rollback.block.action.BlockAction;
import br.com.practice.arena.stage.ArenaStage;
import br.com.practice.user.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.util.List;

public class BlockListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());


        if (user == null || user.getArena() == null || !user.getArena().getStage().equals(ArenaStage.PLAYING)) {
            event.setCancelled(true);
            return;
        }

        Arena arena = user.getArena();

        event.setCancelled(!arena.getGame().isAllowedBuild());

        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        if (block == null || block.getType() == Material.AIR) {
            return;
        }

        BlockAction blockAction = new BlockAction(block, block.getLocation(), block.getState(), BlockAction.ActionType.BREAK);
        arena.getBlockActions().add(blockAction);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null || user.getArena() == null || !user.getArena().getStage().equals(ArenaStage.PLAYING)) {
            event.setCancelled(true);
            return;
        }

        Arena arena = user.getArena();

        event.setCancelled(!arena.getGame().isAllowedBuild());

        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        if (block == null || block.getType() == Material.AIR) {
            return;
        }

        BlockAction blockAction = new BlockAction(block, block.getLocation(), block.getState(), BlockAction.ActionType.PLACE);

        arena.getBlockActions().add(blockAction);
    }

    @EventHandler
    public void onBlockFlow(BlockFromToEvent event) {

        Arena arena = Practice.getInstance().getArenaStorage().getArenaByWorldName(event.getBlock().getWorld());

        if (arena == null || !arena.getStage().equals(ArenaStage.PLAYING)) {
            event.setCancelled(true);
            return;
        }

        Location from = event.getBlock().getLocation();
        Location to = event.getToBlock().getLocation();

        BlockAction actionTo = new BlockAction(to.getBlock(), to.getBlock().getLocation(), to.getBlock().getState(), BlockAction.ActionType.PLACE);
        BlockAction actionFrom = new BlockAction(from.getBlock(), from.getBlock().getLocation(), from.getBlock().getState(), BlockAction.ActionType.PLACE);

        arena.getBlockActions().add(actionTo);
        arena.getBlockActions().add(actionFrom);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplode(EntityExplodeEvent event) {

        Arena arena = Practice.getInstance().getArenaStorage().getArenaByWorldName(event.getEntity().getWorld());

        if (arena == null || !arena.getStage().equals(ArenaStage.PLAYING)) {
            event.setCancelled(true);
            return;
        }

        List<Block> blocksAffectedByExplosion = event.blockList();

        for (Block block : blocksAffectedByExplosion) {
            BlockAction action = new BlockAction(block, block.getLocation(), block.getState(), BlockAction.ActionType.BREAK);

            arena.getBlockActions().add(action);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {

        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null || user.getArena() == null || !user.getArena().getStage().equals(ArenaStage.PLAYING)) {
            event.setCancelled(true);
            return;
        }

        Arena arena = user.getArena();

        event.setCancelled(!arena.getGame().isAllowedBuild());

        if (event.isCancelled()) {
            return;
        }

        Block blockFilled = event.getBlockClicked().getRelative(event.getBlockFace());

        BlockAction action = new BlockAction(blockFilled, blockFilled.getLocation(), blockFilled.getState(), BlockAction.ActionType.PLACE);

        arena.getBlockActions().add(action);
    }

    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }
}
