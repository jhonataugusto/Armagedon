package br.com.armagedon.lobby.main;

import br.com.armagedon.Hub;
import br.com.armagedon.lobby.Lobby;
import br.com.armagedon.util.cuboid.Cuboid;
import org.bukkit.Location;
import org.bukkit.WorldBorder;

import static br.com.armagedon.util.cuboid.Cuboid.loadProperties;

public class Main extends Lobby {

    private final Cuboid cuboid;

    public Main(Hub instance) {
        super(instance);

        setSpawn(new Location(getWorld(), 0.5, 60, 0.5, 0, 0));

        cuboid = loadProperties();
        WorldBorder border = getWorld().getWorldBorder();

        border.setCenter(getSpawn());
        border.setSize(450);
    }
}
