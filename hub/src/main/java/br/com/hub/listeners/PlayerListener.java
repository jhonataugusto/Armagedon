package br.com.hub.listeners;

import br.com.core.Core;
import br.com.core.account.enums.rank.Rank;
import br.com.core.data.object.PunishmentDAO;
import br.com.core.data.object.RankDAO;
import br.com.hub.Hub;
import br.com.hub.user.User;
import br.com.hub.util.tag.TagUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.Date;

import static br.com.hub.util.scheduler.SchedulerUtils.async;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        User user = new User(player.getUniqueId());

        Hub.getInstance().getUserStorage().register(user.getUuid(), user);

        user.setLobby(Hub.getInstance().getLobby());

        user.getLobby().handleScoreboard(player);

        TagUtil.loadTag(player, user.getAccount().getRank());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        TagUtil.unloadTag(player);

        Hub.getInstance().getLobby().getLobbyScoreboard().removePlayer(player);
        Hub.getInstance().getUserStorage().unregister(user.getUuid());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        event.setCancelled(!user.isBuilding());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        event.setCancelled(!user.isBuilding());
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        event.setCancelled(!user.isBuilding());
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        event.setCancelled(!user.isBuilding());
    }
}
