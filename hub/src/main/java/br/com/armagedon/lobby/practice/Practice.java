package br.com.armagedon.lobby.practice;

import br.com.armagedon.Hub;
import br.com.armagedon.lobby.Lobby;
import br.com.armagedon.lobby.practice.queue.Queue;
import br.com.armagedon.util.cuboid.Cuboid;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.util.Set;

import static br.com.armagedon.util.cuboid.Cuboid.loadProperties;

@Getter
public class Practice extends Lobby {

    private Queue queue;
    private Cuboid cuboid;


    public Practice(Hub instance) {
        super(instance);

        registerListeners();

        setSpawn(new Location(getWorld(), 0.5, 60, 0.5, 0, 0));

        cuboid = loadProperties();
        WorldBorder border = getWorld().getWorldBorder();

        border.setCenter(getSpawn());
        border.setSize(450);

        queue = new Queue();
    }

    public void registerListeners(){
        Reflections reflections = new Reflections("br.com.armagedon.lobby.practice");
        Set<Class<? extends Listener>> classes = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : classes) {
            try {
                getInstance().getServer().getPluginManager().registerEvents(clazz.newInstance(), getInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
