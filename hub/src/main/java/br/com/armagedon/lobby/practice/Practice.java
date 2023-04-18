package br.com.armagedon.lobby.practice;

import br.com.armagedon.Hub;
import br.com.armagedon.items.PracticeItems;
import br.com.armagedon.lobby.Lobby;
import br.com.armagedon.lobby.practice.queue.Queue;
import br.com.armagedon.util.cuboid.Cuboid;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static br.com.armagedon.util.cuboid.Cuboid.loadProperties;

@Getter
public class Practice extends Lobby implements Listener {

    private Queue queue;
    private Cuboid cuboid;

    public Practice(Hub instance) {
        super(instance);

        setSpawn(new Location(getWorld(), 0.5, 60, 0.5, 0, 0));

        cuboid = loadProperties();
        WorldBorder border = getWorld().getWorldBorder();

        border.setCenter(getSpawn());
        border.setSize(450);

        getInstance().getServer().getPluginManager().registerEvents(this, getInstance());

        queue = new Queue();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (PracticeItems items : PracticeItems.values()) {
            event.getPlayer().getInventory().setItem(items.getPosition(), items.toItemStack());
        }
    }
}
