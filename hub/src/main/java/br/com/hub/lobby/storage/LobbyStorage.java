package br.com.hub.lobby.storage;

import br.com.hub.lobby.Lobby;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class LobbyStorage {

    private static final Reflections reflections = new Reflections("br.com.hub.lobby");
    private static final List<Class<?>> lobbies  = new ArrayList<>(reflections.getSubTypesOf(Lobby.class));

    public static Class<?> getLobby(String name) {
        return lobbies.stream().filter(lobby -> lobby.getSimpleName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
