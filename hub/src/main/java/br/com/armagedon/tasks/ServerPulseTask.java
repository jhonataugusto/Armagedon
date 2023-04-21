package br.com.armagedon.tasks;

import br.com.armagedon.Hub;
import br.com.armagedon.events.ServerPulseEvent;
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
