package br.com.hub.lobby.main;

import br.com.hub.Hub;
import br.com.hub.lobby.Lobby;
import br.com.hub.user.User;
import br.com.hub.util.cuboid.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.util.Set;

@Getter
@Setter
public class Main extends Lobby {

    private Cuboid cuboid;

    public Main(Hub instance) {
        super(instance);
        registerListeners();

        setCuboid(Cuboid.loadProperties(Bukkit.getWorldContainer()));
        setSpawn(new Location(getWorld(), getCuboid().getSpawnX(), getCuboid().getSpawnY(), getCuboid().getSpawnZ(), (float) getCuboid().getSpawnYaw(), (float) getCuboid().getSpawnPitch()));

        WorldBorder border = getWorld().getWorldBorder();
        border.setCenter(getSpawn());
        border.setSize(450);
    }

    public void registerListeners() {
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
}
