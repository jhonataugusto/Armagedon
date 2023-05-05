package br.com.hub.lobby;

import br.com.core.Core;
import br.com.hub.Hub;
import br.com.hub.lobby.mode.LobbyMode;
import br.com.hub.tasks.ServerPulseTask;
import br.com.hub.user.User;
import br.com.hub.util.bungee.BungeeUtils;
import co.aikar.commands.BaseCommand;
import dev.jcsoftware.jscoreboards.JGlobalScoreboard;
import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.reflections.Reflections;

import java.util.*;

import static br.com.hub.util.scheduler.SchedulerUtils.sync;

@Getter
@Setter
public abstract class Lobby {
    private final Hub instance;
    private final LobbyMode mode;
    private final int id;
    private final World world;
    private Location spawn;
    private ServerPulseTask task;
    private JGlobalScoreboard lobbyScoreboard;

    public Lobby(Hub instance) {

        this.instance = instance;
        this.mode = LobbyMode.getByName(instance.getConfig().getString("lobby.mode"));
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
        world.getEntities().forEach(Entity::remove);
        sync(world::save);
        setSpawn(world.getSpawnLocation());

        createScoreboard();

        task = new ServerPulseTask(getInstance());
        task.runTaskTimer(getInstance(), 0, 20L);
    }

    public void handleScoreboard(Player player) {
        getLobbyScoreboard().addPlayer(player);
    }

    public void createScoreboard(){
        setLobbyScoreboard(new JGlobalScoreboard(
                () -> "§l" + Core.SERVER_NAME.toUpperCase(),
                () -> {
                    String lobbyName = this.getMode().getName() + " #" + this.getId();
                    return Arrays.asList(
                            "",
                            "Lobby: " + ChatColor.AQUA + lobbyName,
                            "",
                            Core.SERVER_WEBSITE
                    );
                }
        ));
    }

    public void loadListeners() {
        Reflections reflections = new Reflections("br.com.hub.listeners");
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
        Reflections reflections = new Reflections("br.com.hub.commands");
        Set<Class<? extends BaseCommand>> commands = reflections.getSubTypesOf(BaseCommand.class);

        for (Class<?> clazz : commands) {
            try {
                getInstance().getBukkitCommandManager().registerCommand((BaseCommand) clazz.newInstance());
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

    public void registerPluginChannels() {
        getInstance().getServer().getMessenger().registerOutgoingPluginChannel(getInstance(), Core.BUNGEECORD_MESSAGING_CHANNEL);
        getInstance().getServer().getMessenger().registerIncomingPluginChannel(getInstance(), Core.BUNGEECORD_MESSAGING_CHANNEL, new BungeeUtils());
    }
}
