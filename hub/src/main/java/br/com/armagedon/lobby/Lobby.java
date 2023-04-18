package br.com.armagedon.lobby;

import br.com.armagedon.Core;
import br.com.armagedon.Hub;
import br.com.armagedon.commands.ACommand;
import br.com.armagedon.lobby.mode.LobbyMode;
import br.com.armagedon.tasks.ServerHeartBeatTask;
import br.com.armagedon.util.bungee.BungeeUtils;
import lombok.Getter;
import lombok.Setter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.reflections.Reflections;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public abstract class Lobby {
    private final Hub instance;
    private final LobbyMode mode;
    private final int id;
    private final World world;
    private Location spawn;

    private ServerHeartBeatTask task;

    public Lobby(Hub instance) {

        this.instance = instance;
        this.mode = LobbyMode.fromString(instance.getConfig().getString("lobby.mode"));
        this.id = instance.getConfig().getInt("lobby.id");
        this.world = instance.getServer().getWorlds().get(0);

        this.world.setTime(6000L);
        this.world.setDifficulty(Difficulty.PEACEFUL);
        this.world.setAutoSave(false);
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.world.setGameRuleValue("doFireTick", "false");
        this.world.setGameRuleValue("mobGriefing", "false");
        this.world.setGameRuleValue("doTileDrops", "false");
        this.world.setGameRuleValue("doEntityDrops", "false");
        this.world.setGameRuleValue("commandBlockOutput", "false");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setGameRuleValue("logAdminCommands", "false");
        this.world.setGameRuleValue("randomTickSpeed", "0");

        this.spawn = world.getSpawnLocation();

        task = new ServerHeartBeatTask(getInstance());

        task.runTaskTimer(getInstance(),0,20L);
    }

    public void loadListeners() {
        Reflections reflections = new Reflections("br.com.armagedon.listeners");
        Set<Class<? extends Listener>> classes = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : classes) {
            try {
                getInstance().getServer().getPluginManager().registerEvents(clazz.newInstance(), getInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void registerCommands() {

        BukkitFrame bukkitFrame = new BukkitFrame(getInstance());
        Reflections reflections = new Reflections("br.com.armagedon.commands");
        Set<Class<? extends ACommand>> commands = reflections.getSubTypesOf(ACommand.class);

        for (Class<?> clazz : commands) {
            try {
                bukkitFrame.registerCommands(clazz.newInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void removeRecipes() {
        Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();

        while (iterator.hasNext()) {

            Recipe recipe = iterator.next();

            if (recipe.getResult() != null) {
                iterator.remove();
            }
        }
    }

    public void registerPluginChannels(){
        getInstance().getServer().getMessenger().registerOutgoingPluginChannel(getInstance(), Core.BUNGEECORD_MESSAGING_CHANNEL);
        getInstance().getServer().getMessenger().registerIncomingPluginChannel(getInstance(), Core.BUNGEECORD_MESSAGING_CHANNEL, new BungeeUtils());
    }
}
