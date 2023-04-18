package br.com.armagedon.lobby.main;

import br.com.armagedon.Hub;
import br.com.armagedon.gui.ServerGUI;
import br.com.armagedon.items.LobbyItems;
import br.com.armagedon.items.PracticeItems;
import br.com.armagedon.lobby.Lobby;
import br.com.armagedon.util.cuboid.Cuboid;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static br.com.armagedon.util.cuboid.Cuboid.loadProperties;

public class Main extends Lobby implements Listener {

    private final Cuboid cuboid;


    public Main(Hub instance) {

        super(instance);

        setSpawn(new Location(getWorld(), 0.5, 60, 0.5, 0, 0));

        cuboid = loadProperties();
        WorldBorder border = getWorld().getWorldBorder();

        border.setCenter(getSpawn());
        border.setSize(450);

        getInstance().getServer().getPluginManager().registerEvents(this, getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (LobbyItems items : LobbyItems.values()) {
            event.getPlayer().getInventory().setItem(items.getPosition(), items.toItemStack());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        NBT.modify(event.getItem(), nbt -> {
            if (nbt.hasCustomNbtData()) {

                if (nbt.getString(LobbyItems.SERVER_LIST.getKey()).equals(LobbyItems.SERVER_LIST.getValue())) {
                    ServerGUI.INVENTORY.open(event.getPlayer());
                }
            }
        });
    }
}
