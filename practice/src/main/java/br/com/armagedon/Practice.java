package br.com.armagedon;

import br.com.armagedon.arena.storage.ArenaStorage;
import br.com.armagedon.enums.game.GameMode;
import br.com.armagedon.enums.map.Maps;
import br.com.armagedon.game.storage.GameStorage;
import br.com.armagedon.user.storage.UserStorage;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static br.com.armagedon.util.async.AsyncUtils.async;

@Getter
public class Practice extends JavaPlugin {

    private static Practice instance;
    private UserStorage userStorage;
    private ArenaStorage arenaStorage;
    private GameStorage gameStorage;

    @Override
    public void onLoad() {
        super.onLoad();

        instance = this;

        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        registerEvents();

        userStorage = new UserStorage();

        arenaStorage = new ArenaStorage();

        gameStorage = new GameStorage();

        gameStorage.load();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Practice getInstance() {
        return instance;
    }

    public void registerEvents() {
        Reflections reflections = new Reflections("br.com.armagedon.listener");

        Set<Class<? extends Listener>> listeners = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : listeners) {
            try {
                this.getServer().getPluginManager().registerEvents(clazz.newInstance(), this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
