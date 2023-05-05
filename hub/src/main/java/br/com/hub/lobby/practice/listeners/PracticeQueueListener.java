package br.com.hub.lobby.practice.listeners;

import br.com.hub.Hub;
import br.com.core.crud.redis.DuelContextRedisCRUD;
import br.com.core.data.DuelData;
import br.com.core.enums.server.Server;
import br.com.hub.events.PlayerEnterQueueEvent;
import br.com.hub.events.PlayerLeaveQueueEvent;
import br.com.hub.events.QueueMatchEvent;
import br.com.hub.events.ServerPulseEvent;
import br.com.core.enums.game.GameMode;
import br.com.hub.gui.game.PracticeGUI;
import br.com.hub.items.PracticeItems;
import br.com.hub.items.QueueItems;
import br.com.hub.lobby.practice.Practice;
import br.com.hub.lobby.practice.queue.properties.DuelProperties;
import br.com.core.enums.map.Maps;
import br.com.hub.user.User;
import br.com.hub.util.bungee.BungeeUtils;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static br.com.hub.util.scheduler.SchedulerUtils.async;

@Getter
public class PracticeQueueListener implements Listener {

    @EventHandler
    public void onQueueEnterEvent(PlayerEnterQueueEvent event) {
        event.getUser().getPlayer().sendMessage(ChatColor.YELLOW + "Você entrou na fila! Por favor, aguarde pacientemente enquanto procuramos um oponente para você.");

        event.getUser().getPlayer().closeInventory();

        event.getUser().getPlayer().getInventory().clear();

        for (QueueItems item : QueueItems.values()) {
            event.getUser().getPlayer().getInventory().setItem(item.getPosition(), item.toItemStack());
        }

    }

    @EventHandler
    public void onQueueLeaveEvent(PlayerLeaveQueueEvent event) {
        event.getUser().getPlayer().sendMessage(ChatColor.YELLOW + "Você saiu da fila!");

        event.getUser().getPlayer().getInventory().clear();

        for (PracticeItems item : PracticeItems.values()) {
            event.getUser().getPlayer().getInventory().setItem(item.getPosition(), item.toItemStack());
        }
    }

    @EventHandler
    public void onQueueMatchEvent(QueueMatchEvent event) {
        User user1 = event.getPlayer1();
        User user2 = event.getPlayer2();

        if (user1 == null || user2 == null) {
            return;
        }

        user1.getPlayer().getInventory().clear();
        user2.getPlayer().getInventory().clear();

        DuelData duelContext = new DuelData();

        duelContext.getTeam1().add(user1.getUuid());
        duelContext.getTeam2().add(user2.getUuid());

        DuelProperties properties = event.getProperties();

        duelContext.setCustom(false);
        duelContext.setRanked(properties.isRanked());
        duelContext.setGameModeName(properties.getMode());

        GameMode mode = GameMode.getByName(duelContext.getGameModeName());
        String mapName = Maps.getRandomMap(mode).getName();

        duelContext.setMapName(mapName);

        DuelContextRedisCRUD.save(duelContext);

        async(() -> {
            BungeeUtils.connect(user1.getPlayer(), Server.PRACTICE);
            BungeeUtils.connect(user2.getPlayer(), Server.PRACTICE);
        });
    }


    @EventHandler
    public void onServerHeartBeat(ServerPulseEvent event) {
        Practice instance = (Practice) Hub.getInstance().getLobby();
        instance.getQueue().search();
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.hasItem() || !event.getAction().name().contains("RIGHT_")) {
            return;
        }

        Practice instance = (Practice) Hub.getInstance().getLobby();

        ItemStack item = event.getItem();

        if (item == null || item.getType() == Material.AIR || item.getAmount() == 0) {
            event.setCancelled(true);
            return;
        }

        NBT.modify(event.getItem(), nbt -> {
            if (nbt.hasCustomNbtData()) {

                if (nbt.getString(QueueItems.QUIT_QUEUE.getKey()).equals(QueueItems.QUIT_QUEUE.getValue())) {

                    User user = User.fetch(event.getPlayer().getUniqueId());

                    if (instance.getQueue().inQueue(user)) {
                        instance.getQueue().leave(user);
                    }
                    return;
                }

                if (nbt.getString(PracticeItems.RANKED_UNRANKED.getKEY()).equals(PracticeItems.RANKED_UNRANKED.getValue())) {
                    User user = User.fetch(event.getPlayer().getUniqueId());
                    if (!instance.getQueue().inQueue(user)) {
                        PracticeGUI.INVENTORY.open(user.getPlayer());
                    }

                }
            }
        });
    }
}
