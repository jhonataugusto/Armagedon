package br.com.practice;

import br.com.core.Core;
import br.com.core.crud.redis.DuelRedisCRUD;
import br.com.core.data.object.PlayerCheckDAO;
import br.com.core.database.redis.RedisChannels;
import br.com.core.utils.pubsub.RedisPubSubUtil;
import br.com.practice.arena.storage.ArenaStorage;
import br.com.practice.game.storage.GameStorage;
import br.com.practice.task.ArenaPulseTask;
import br.com.practice.user.User;
import br.com.practice.user.storage.UserStorage;
import br.com.practice.util.bungee.BungeeUtils;
import br.com.practice.util.file.CompressionUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.File;
import java.util.Set;
import java.util.UUID;

import static br.com.practice.util.scheduler.SchedulerUtils.async;

@Getter
public class Practice extends JavaPlugin {

    private static Practice instance;
    private UserStorage userStorage;
    private ArenaStorage arenaStorage;
    private GameStorage gameStorage;
    private BukkitCommandManager commandManager;
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

        deleteUncachedArenas();


        commandManager = new BukkitCommandManager(this);
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

        DuelRedisCRUD.refreshDuels();

        async(() -> {
            RedisPubSubUtil.subscribe((channel, message) -> {

                String minecraftAntiCheatChannel = RedisChannels.MINECRAFT_ANTICHEAT_MESSAGES_CHANNEL.getChannel();
                if (!channel.equalsIgnoreCase(minecraftAntiCheatChannel)) {
                    return;
                }

                PlayerCheckDAO playerCheck = Core.GSON.fromJson(message, PlayerCheckDAO.class);

                User user = User.fetch(UUID.fromString(playerCheck.getUuid()));

                if (user == null) {
                    return;
                }

                user.setRange(playerCheck.getRange());
                user.getPlayer().getWorld().playSound(user.getPlayer().getLocation(), Sound.BAT_DEATH, 3.5f, 3.5f);
                Bukkit.broadcastMessage(playerCheck.getRange() + "");

            }, RedisChannels.MINECRAFT_ANTICHEAT_MESSAGES_CHANNEL.getChannel());
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getArenaStorage().getArenas().forEach((id, arena) -> getArenaStorage().unload(id));
        getUserStorage().getUsers().clear();
        getGameStorage().unload();
        DuelRedisCRUD.refreshDuels();
        Bukkit.getScheduler().cancelAllTasks();
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

        Set<Class<? extends BaseCommand>> commands = reflections.getSubTypesOf(BaseCommand.class);

        for (Class<? extends BaseCommand> clazz : commands) {
            try {
                getCommandManager().registerCommand(clazz.newInstance());
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


    public void deleteUncachedArenas() {
        File arenasFolder = new File(getServer().getWorldContainer(), "arenas");

        if (!arenasFolder.exists()) {
            arenasFolder.mkdirs();
        }

        for (File file : arenasFolder.listFiles()) {
            if (file.isDirectory() && !file.getName().equals("presets")) {
                CompressionUtil.delete(file);

            } else if (file.isFile()) {
                file.delete();
            }
        }
    }
}
