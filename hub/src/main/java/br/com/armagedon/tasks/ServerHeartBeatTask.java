package br.com.armagedon.tasks;

import br.com.armagedon.Hub;
import br.com.armagedon.events.ServerHeartBeatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ServerHeartBeatTask extends BukkitRunnable {

    private final Hub instance;

    public ServerHeartBeatTask(Hub instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        ServerHeartBeatEvent event = new ServerHeartBeatEvent();
        instance.getServer().getPluginManager().callEvent(event);

    }
}
