package br.com.bungee;

import br.com.core.account.storage.AccountStorage;
import br.com.bungee.listeners.ServerListener;
import br.com.bungee.task.HeartBeatTask;
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

        accountStorage.getAccounts().forEach((key, value) -> accountStorage.unregister(value.getUuid()));
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