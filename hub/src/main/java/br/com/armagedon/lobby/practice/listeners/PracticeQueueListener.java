package br.com.armagedon.lobby.practice.listeners;

import br.com.armagedon.Hub;
import br.com.armagedon.crud.redis.DuelContextRedisCRUD;
import br.com.armagedon.data.DuelContextData;
import br.com.armagedon.enums.server.Server;
import br.com.armagedon.events.PlayerEnterQueueEvent;
import br.com.armagedon.events.PlayerLeaveQueueEvent;
import br.com.armagedon.events.QueueMatchEvent;
import br.com.armagedon.events.ServerPulseEvent;
import br.com.armagedon.enums.game.GameMode;
import br.com.armagedon.gui.PracticeGUI;
import br.com.armagedon.items.PracticeItems;
import br.com.armagedon.items.QueueItems;
import br.com.armagedon.lobby.practice.Practice;
import br.com.armagedon.lobby.practice.queue.properties.QueueProperties;
import br.com.armagedon.enums.map.Maps;
import br.com.armagedon.user.User;
import br.com.armagedon.util.bungee.BungeeUtils;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public class PracticeQueueListener implements Listener {

    @EventHandler
    public void onQueueEnterEvent(PlayerEnterQueueEvent event) {
        event.getUser().getPlayer().sendMessage("entrou na queue");

        event.getUser().getPlayer().closeInventory();

        event.getUser().getPlayer().getInventory().clear();

        for (QueueItems item : QueueItems.values()) {
            event.getUser().getPlayer().getInventory().setItem(item.getPosition(), item.toItemStack());
        }

    }

    @EventHandler
    public void onQueueLeaveEvent(PlayerLeaveQueueEvent event) {
        event.getUser().getPlayer().sendMessage("saiu da queue");

        event.getUser().getPlayer().getInventory().clear();

        for (PracticeItems item : PracticeItems.values()) {
            event.getUser().getPlayer().getInventory().setItem(item.getPosition(), item.toItemStack());
        }
    }

    @EventHandler
    public void onQueueMatchEvent(QueueMatchEvent event) {
        User user1 = event.getPlayer1();
        User user2 = event.getPlayer2();

        if(user1 == null || user2 == null) {
            return;
        }

        DuelContextData duelContext = new DuelContextData();

        duelContext.getTeam1().add(user1.getUuid());
        duelContext.getTeam2().add(user2.getUuid());

        QueueProperties properties = event.getProperties();

        duelContext.setCustom(false);
        duelContext.setRanked(properties.isRanked());
        duelContext.setGameMode(properties.getMode());

        GameMode mode = GameMode.getByName(duelContext.getGameMode());
        String mapName = Maps.getRandomMap(mode).getName();

        duelContext.setMapName(mapName);

        DuelContextRedisCRUD.save(duelContext);

        BungeeUtils.connect(user1.getPlayer(), Server.PRACTICE);
        BungeeUtils.connect(user2.getPlayer(), Server.PRACTICE);
    }


    @EventHandler
    public void onServerHeartBeat(ServerPulseEvent event) {
        Practice instance = (Practice) Hub.getInstance().getLobby();
        instance.getQueue().search();
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

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
