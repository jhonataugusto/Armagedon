package br.com.practice.listener;

import br.com.core.enums.server.Server;
import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import br.com.practice.events.spectator.SpectatorEnterArenaEvent;
import br.com.practice.events.spectator.SpectatorLeaveArenaEvent;
import br.com.practice.items.SpectatorItems;
import br.com.practice.spectator.Spectator;
import br.com.practice.user.User;
import br.com.practice.util.bungee.BungeeUtils;
import br.com.practice.util.visibility.Visibility;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import static br.com.practice.util.scheduler.SchedulerUtils.async;
import static br.com.practice.util.scheduler.SchedulerUtils.delay;

public class SpectatorListener implements Listener {
    @EventHandler
    public void onSpectatorEnter(SpectatorEnterArenaEvent event) {
        Player spectator = event.getSpectator().getPlayer();
        Arena arena = event.getArena();

        event.getSpectator().setArena(arena);

        boolean isPresent = arena.getAllTeamMembers().stream().findAny().isPresent();

        if (!isPresent) {
            return;
        }

        User anyUserOnThisArena = arena.getAllTeamMembers().stream().findAny().get();

        spectator.teleport(anyUserOnThisArena.getPlayer().getLocation());

        Visibility.invisible(spectator, arena.getAllTeamMembers());

        spectator.setAllowFlight(true);
        spectator.setFlying(true);
        spectator.setHealth(20);
        spectator.getInventory().clear();
        spectator.getInventory().setArmorContents(null);
        spectator.getActivePotionEffects().forEach(potionEffect -> spectator.getPlayer().removePotionEffect(potionEffect.getType()));


        spectator.sendMessage("Assistindo a arena: " + ChatColor.GRAY + arena.getDisplayArenaId());

        for (SpectatorItems items : SpectatorItems.values()) {
            spectator.getInventory().setItem(items.getPosition(), items.toItemStack());
        }

        event.getArena().getCurrentSpectators().add(event.getSpectator());

        event.getArena().getData().getRegisteredSpectators().add(event.getSpectator().getUuid());
        event.getArena().getData().getSpectators().remove(event.getSpectator().getUuid());

        async(() -> event.getArena().getData().saveData());

        event.getArena().handleScoreboard(event.getSpectator());
    }

    @EventHandler
    public void onSpectatorLeave(SpectatorLeaveArenaEvent event) {
        event.getSpectator().setArena(null);

        event.getArena().getCurrentSpectators().remove(event.getSpectator());
        event.getArena().removeScoreboard(event.getSpectator());
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        if (!user.isSpectating()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        if (!user.isSpectating()) {
            return;
        }

        event.setCancelled(true);
    }


    @EventHandler
    public void onClickItemSpectator(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        if (!event.hasItem() || !event.getAction().name().contains("RIGHT_")) {
            return;
        }

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }

        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        if (!user.isSpectating()) {
            return;
        }

        Arena arena = user.getArena();

        if (arena == null) {
            return;
        }

        if (arena.getStage() != ArenaStage.PLAYING && arena.getStage() != ArenaStage.ENDING) {
            return;
        }


        NBT.modify(item, nbt -> {

            if (nbt.hasCustomNbtData()) {

                if (nbt.getString(SpectatorItems.BACK_TO_LOBBY.getKey()).equals(SpectatorItems.BACK_TO_LOBBY.getValue())) {

                    BungeeUtils.connect(player, Server.LOBBY_PRACTICE);
                    Spectator.unspectate(player, arena);
                }

            }
        });

    }

    @EventHandler
    public void onPlayerFoodChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        if (player == null) {
            return;
        }

        User user = User.fetch(player.getUniqueId());

        if (user == null || !user.isSpectating()) {
            return;
        }

        event.setCancelled(true);
    }
}
