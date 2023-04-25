package br.com.hub;

import br.com.core.Core;
import br.com.hub.lobby.Lobby;
import br.com.hub.lobby.storage.LobbyStorage;
import br.com.hub.user.storage.UserStorage;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public class Hub extends JavaPlugin {

    private static Hub instance;
    private UserStorage userStorage;
    private InventoryManager inventoryManager;
    private Lobby lobby;

    private String CHANNEL = "REDIRECT";

    @Override
    public void onLoad() {
        instance = this;
        Core.MONGO_LOGGER.setLevel(Level.OFF);
    }

    @Override
    public void onEnable() {

        instance = this;

        userStorage = new UserStorage();

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        lobby = loadLobby();

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
    }

    public static Hub getInstance() {
        return instance;
    }

    protected Lobby loadLobby() {
        try {
            Lobby lobby = (Lobby) LobbyStorage.getLobby(Hub.getInstance().getConfig().getString("lobby.mode")).getConstructor(Hub.class).newInstance(this);

            lobby.loadListeners();
            lobby.registerCommands();
            lobby.registerPluginChannels();
            lobby.removeRecipes();

            return lobby;
        } catch (Exception exception) {
            exception.printStackTrace();

            Bukkit.shutdown();
        }
        return null;
    }
}
