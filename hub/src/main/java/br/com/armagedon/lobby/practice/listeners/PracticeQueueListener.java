package br.com.armagedon.lobby.practice.listeners;

import br.com.armagedon.Hub;
import br.com.armagedon.events.PlayerEnterQueueEvent;
import br.com.armagedon.events.PlayerLeaveQueueEvent;
import br.com.armagedon.events.QueueMatchEvent;
import br.com.armagedon.events.ServerHeartBeatEvent;
import br.com.armagedon.gui.PracticeGUI;
import br.com.armagedon.items.PracticeItems;
import br.com.armagedon.items.QueueItems;
import br.com.armagedon.lobby.practice.Practice;
import br.com.armagedon.user.User;
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
        event.getPlayer1().getPlayer().sendMessage("a queue deu match!" + event.getPlayer1().getPlayer().getName());
        event.getPlayer2().getPlayer().sendMessage("a queue deu match!" + event.getPlayer2().getPlayer().getName());
    }


    @EventHandler
    public void onServerHeartBeat(ServerHeartBeatEvent event) {
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
