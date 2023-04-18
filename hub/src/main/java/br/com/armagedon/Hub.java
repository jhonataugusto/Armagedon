package br.com.armagedon;

import br.com.armagedon.lobby.Lobby;
import br.com.armagedon.lobby.storage.LobbyStorage;
import br.com.armagedon.util.bungee.BungeeUtils;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import br.com.armagedon.user.storage.UserStorage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
