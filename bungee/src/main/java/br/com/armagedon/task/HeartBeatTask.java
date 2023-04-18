package br.com.armagedon.task;

import br.com.armagedon.events.HeartBeatEvent;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class HeartBeatTask implements Runnable {

    private final Plugin plugin;


    public HeartBeatTask(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        HeartBeatEvent heartBeatEvent = new HeartBeatEvent();
        ProxyServer.getInstance().getPluginManager().callEvent(heartBeatEvent);
    }
}
