package br.com.armagedon.lobby.storage;

import br.com.armagedon.lobby.Lobby;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LobbyStorage {

    private static final Reflections reflections = new Reflections("br.com.armagedon.lobby");
    private static final List<Class<?>> lobbies  = new ArrayList<>(reflections.getSubTypesOf(Lobby.class));

    public static Class<?> getLobby(String name) {
        return lobbies.stream().filter(lobby -> lobby.getSimpleName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
