package br.com.practice;

import br.com.core.Core;
import br.com.practice.arena.storage.ArenaStorage;
import br.com.core.holder.command.ACommand;
import br.com.practice.game.storage.GameStorage;
import br.com.practice.task.ArenaPulseTask;
import br.com.practice.user.storage.UserStorage;
import br.com.practice.util.bungee.BungeeUtils;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.Set;

@Getter
public class Practice extends JavaPlugin {

    private static Practice instance;
    private UserStorage userStorage;
    private ArenaStorage arenaStorage;
    private GameStorage gameStorage;

    private BukkitFrame commandFramework;
    private InventoryManager inventoryManager;

    @Override
    public void onLoad() {
        super.onLoad();

        instance = this;

        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        commandFramework = new BukkitFrame(this);
        registerCommands();

        registerEvents();
        registerPluginChannels();
        registerSchedulers();

        userStorage = new UserStorage();
        arenaStorage = new ArenaStorage();
        gameStorage = new GameStorage();
        gameStorage.load();

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Practice getInstance() {
        return instance;
    }

    public void registerEvents() {
        Reflections reflections = new Reflections("br.com.practice.listener");

        Set<Class<? extends Listener>> listeners = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : listeners) {
            try {
                this.getServer().getPluginManager().registerEvents(clazz.newInstance(), this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void registerCommands() {
        Reflections reflections = new Reflections("br.com.practice.commands");

        Set<Class<? extends ACommand>> commands = reflections.getSubTypesOf(ACommand.class);

        for (Class<? extends ACommand> clazz : commands) {
            try {
                this.getCommandFramework().registerCommands(clazz.newInstance());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public void registerSchedulers() {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ArenaPulseTask(this), 0, 20);
    }

    public void registerPluginChannels() {
        getInstance().getServer().getMessenger().registerOutgoingPluginChannel(getInstance(), Core.BUNGEECORD_MESSAGING_CHANNEL);
        getInstance().getServer().getMessenger().registerIncomingPluginChannel(getInstance(), Core.BUNGEECORD_MESSAGING_CHANNEL, new BungeeUtils());
    }
}
