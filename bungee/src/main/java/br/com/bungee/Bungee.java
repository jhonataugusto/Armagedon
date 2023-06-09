package br.com.bungee;

import br.com.bungee.events.RedisPubSubEvent;
import br.com.core.account.storage.AccountStorage;
import br.com.bungee.task.HeartBeatTask;
import br.com.core.database.redis.RedisChannels;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.reflections.Reflections;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static br.com.bungee.util.scheduler.SchedulerUtils.async;
import static br.com.core.utils.pubsub.RedisPubSubUtil.subscribe;

@Getter
public class Bungee extends Plugin {

    private static Bungee instance;

    private AccountStorage accountStorage;
    private BungeeCommandManager bungeeCommandManager;


    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        accountStorage = new AccountStorage();

        registerEvents();
        registerSchedulers();

        bungeeCommandManager = new BungeeCommandManager(this);
        registerCommands();

        async(() -> {
            subscribe((channel, message) -> {
                new RedisPubSubEvent().setChannel(channel).setMessage(message).call();
            }, RedisChannels.MINECRAFT_RECEIVE_MESSAGES_CHANNEL.getChannel());
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
        accountStorage.getAccounts().forEach((key, value) -> accountStorage.unregister(value.getUuid()));
    }

    public void registerCommands() {
        Reflections reflections = new Reflections("br.com.bungee.commands");
        Set<Class<? extends BaseCommand>> commands = reflections.getSubTypesOf(BaseCommand.class);

        for (Class<? extends BaseCommand> clazz : commands) {
            try {
                bungeeCommandManager.registerCommand(clazz.newInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void registerEvents() {
        Reflections reflections = new Reflections("br.com.bungee.listeners");
        Set<Class<? extends Listener>> commands = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : commands) {
            try {
                ProxyServer.getInstance().getPluginManager().registerListener(this, clazz.newInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void registerSchedulers() {
        this.getProxy().getScheduler().schedule(this, new HeartBeatTask(this), 0, 5, TimeUnit.SECONDS);
    }

    public static Bungee getInstance() {
        return instance;
    }
}