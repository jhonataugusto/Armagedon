package br.com.armagedon;

import br.com.armagedon.account.storage.AccountStorage;
import br.com.armagedon.listeners.ServerListener;
import br.com.armagedon.task.HeartBeatTask;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

@Getter
public class Bungee extends Plugin {

    private static Bungee instance;

    private AccountStorage accountStorage;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        accountStorage = new AccountStorage();
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

    public static Bungee getInstance() {
        return instance;
    }
}