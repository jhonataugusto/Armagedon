package br.com.armagedon.lobby.main;

import br.com.armagedon.Hub;
import br.com.armagedon.gui.ServerGUI;
import br.com.armagedon.items.LobbyItems;
import br.com.armagedon.items.PracticeItems;
import br.com.armagedon.lobby.Lobby;
import br.com.armagedon.util.cuboid.Cuboid;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;

import java.util.Set;

import static br.com.armagedon.util.cuboid.Cuboid.loadProperties;

public class Main extends Lobby {

    private final Cuboid cuboid;


    public Main(Hub instance) {
        super(instance);

        registerListeners();

        setSpawn(new Location(getWorld(), 0.5, 60, 0.5, 0, 0));

        cuboid = loadProperties();
        WorldBorder border = getWorld().getWorldBorder();

        border.setCenter(getSpawn());
        border.setSize(450);
    }

    public void registerListeners(){
        Reflections reflections = new Reflections("br.com.armagedon.lobby.main");
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
