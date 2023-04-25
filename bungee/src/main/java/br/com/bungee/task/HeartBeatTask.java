package br.com.bungee.task;

import br.com.bungee.events.ProxyPulseEvent;
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
        ProxyPulseEvent proxyPulseEvent = new ProxyPulseEvent();
        ProxyServer.getInstance().getPluginManager().callEvent(proxyPulseEvent);
    }
}
