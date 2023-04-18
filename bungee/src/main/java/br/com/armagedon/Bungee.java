package br.com.armagedon;

import br.com.armagedon.listeners.ServerListener;
import br.com.armagedon.task.HeartBeatTask;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class Bungee extends Plugin {

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        registerEvents();
        registerSchedulers();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void registerEvents(){
        this.getProxy().getPluginManager().registerListener(this, new ServerListener());
    }


    public void registerSchedulers(){
        this.getProxy().getScheduler().schedule(this, new HeartBeatTask(this), 0, 5,TimeUnit.SECONDS);
    }

}