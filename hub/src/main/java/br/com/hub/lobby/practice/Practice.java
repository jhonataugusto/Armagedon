package br.com.hub.lobby.practice;

import br.com.hub.Hub;
import br.com.hub.gui.KitEditorGUI;
import br.com.hub.gui.ModeSelectorGUI;
import br.com.hub.lobby.Lobby;
import br.com.hub.lobby.practice.queue.Queue;
import br.com.hub.util.cuboid.Cuboid;
import co.aikar.commands.BaseCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.util.Set;

@Getter
public class Practice extends Lobby {

    private final Queue queue;
    private final Cuboid cuboid;
    private KitEditorGUI kitEditorGUI;
    private ModeSelectorGUI modeSelectorGUI;

    public Practice(Hub instance) {
        super(instance);

        registerListeners();
        registerCustomCommands();

        setSpawn(new Location(getWorld(), 0.5, 60, 0.5, 0, 0));

        cuboid = Cuboid.loadProperties(Bukkit.getWorldContainer());
        WorldBorder border = getWorld().getWorldBorder();

        border.setCenter(getSpawn());
        border.setSize(450);

        queue = new Queue();
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

    @Override
    public void handleScoreboard() {
        super.handleScoreboard();
    }

    @Override
    public void updateScoreboard() {
        super.updateScoreboard();
    }
}
