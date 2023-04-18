package br.com.armagedon.listeners;

import br.com.armagedon.events.PlayerEnterQueueEvent;
import br.com.armagedon.events.PlayerLeaveQueueEvent;
import br.com.armagedon.events.QueueMatchEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QueueListener implements Listener {

    @EventHandler
    public void onQueueEnterEvent(PlayerEnterQueueEvent event) {
        event.getUser().getPlayer().sendMessage("entrou na queue");

    }

    @EventHandler
    public void onQueueLeaveEvent(PlayerLeaveQueueEvent event) {
        event.getUser().getPlayer().sendMessage("saiu da queue");
    }

    @EventHandler
    public void onQueueMatchEvent(QueueMatchEvent event) {
        event.getPlayer1().getPlayer().sendMessage("a queue deu match!" + event.getPlayer1().getPlayer().getName());
        event.getPlayer2().getPlayer().sendMessage("a queue deu match!" + event.getPlayer2().getPlayer().getName());
    }

}
