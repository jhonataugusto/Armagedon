package br.com.hub.lobby.main;

import br.com.hub.Hub;
import br.com.hub.lobby.Lobby;
import br.com.hub.util.cuboid.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.util.Set;

public class Main extends Lobby {

    private final Cuboid cuboid;


    public Main(Hub instance) {
        super(instance);

        registerListeners();

        setSpawn(new Location(getWorld(), 0.5, 60, 0.5, 0, 0));

        cuboid = Cuboid.loadProperties(Bukkit.getWorldContainer());
        WorldBorder border = getWorld().getWorldBorder();

        border.setCenter(getSpawn());
        border.setSize(450);
    }

    public void registerListeners(){
        Reflections reflections = new Reflections("br.com.hub.lobby.main");
        Set<Class<? extends Listener>> classes = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : classes) {
            try {
                getInstance().getServer().getPluginManager().registerEvents(clazz.newInstance(), getInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
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
