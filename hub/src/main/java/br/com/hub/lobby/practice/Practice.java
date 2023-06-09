package br.com.hub.lobby.practice;

import br.com.core.Core;
import br.com.core.crud.redis.DuelRedisCRUD;
import br.com.core.crud.redis.ServerRedisCRUD;
import br.com.core.enums.server.Server;
import br.com.hub.Hub;
import br.com.hub.gui.editor.KitEditorGUI;
import br.com.hub.lobby.Lobby;
import br.com.hub.lobby.practice.queue.Queue;

import br.com.hub.util.cuboid.Cuboid;
import co.aikar.commands.BaseCommand;
import dev.jcsoftware.jscoreboards.JGlobalScoreboard;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.util.*;

@Getter
@Setter
public class Practice extends Lobby {

    private final Queue queue;
    private Cuboid cuboid;

    public Practice(Hub instance) {
        super(instance);
        registerListeners();
        registerCustomCommands();

        setCuboid(Cuboid.loadProperties(Bukkit.getWorldContainer()));
        setSpawn(new Location(getWorld(), getCuboid().getSpawnX(), getCuboid().getSpawnY(), getCuboid().getSpawnZ(), (float) getCuboid().getSpawnYaw(), (float) getCuboid().getSpawnPitch()));

        createScoreboard();

        WorldBorder border = getWorld().getWorldBorder();
        border.setCenter(getSpawn());
        border.setSize(450);

        queue = new Queue();

        DuelRedisCRUD.refreshDuels();
    }

    @Override
    public void handleScoreboard(Player player) {
        Hub.getInstance().getLobby().getLobbyScoreboard().addPlayer(player);
    }

    @Override
    public void createScoreboard() {
        setLobbyScoreboard(new JGlobalScoreboard(

                () -> "§l" + "practice".toUpperCase(),

                () -> {

                    int playing = ServerRedisCRUD.findByName(Server.PRACTICE.getName()).getCurrentPlayers();

                    int totalPlayers = Bukkit.getOnlinePlayers().size() + playing;

                    return Arrays.asList(
                            "",
                            "Online: " + ChatColor.AQUA + totalPlayers,
                            "Jogando: " + ChatColor.AQUA + playing,
                            "",
                            Core.SERVER_WEBSITE
                    );
                }
        ));
    }

    public void registerCustomCommands() {
        Reflections reflections = new Reflections("br.com.hub.lobby.practice.commands");
        Set<Class<? extends BaseCommand>> commands = reflections.getSubTypesOf(BaseCommand.class);

        for (Class<?> clazz : commands) {
            try {
                getInstance().getBukkitCommandManager().registerCommand((BaseCommand) clazz.newInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void registerListeners() {
        Reflections reflections = new Reflections("br.com.hub.lobby.practice");
        Set<Class<? extends Listener>> classes = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : classes) {
            try {
                getInstance().getServer().getPluginManager().registerEvents(clazz.newInstance(), getInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        getInstance().getServer().getPluginManager().registerEvents(KitEditorGUI.getInstance(), getInstance());
    }
}
