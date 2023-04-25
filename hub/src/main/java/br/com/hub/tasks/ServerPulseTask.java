package br.com.hub.tasks;

import br.com.hub.Hub;
import br.com.hub.events.ServerPulseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerPulseTask extends BukkitRunnable {

    private final Hub instance;

    public ServerPulseTask(Hub instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        ServerPulseEvent event = new ServerPulseEvent();
        instance.getServer().getPluginManager().callEvent(event);
    }
}
