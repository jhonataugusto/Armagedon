package br.com.core.enums.server;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Getter
public enum Server {
    LOBBY("lobby", "Lobby"),
    LOBBY_PRACTICE("lobby_practice", "Lobby Practice"),
    PRACTICE("practice", "Practice"),
    UNKOWN("unkown", "Desconhecido");

    private final String name;
    private final String displayName;
    public HashMap<Server, List<Server>> pairs;

    Server(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    static {
        LOBBY.pairs = new HashMap<>();
        LOBBY.pairs.put(LOBBY, Arrays.asList(UNKOWN));

        LOBBY_PRACTICE.pairs = new HashMap<>();
        LOBBY_PRACTICE.pairs.put(LOBBY_PRACTICE, Arrays.asList(PRACTICE));

        PRACTICE.pairs = new HashMap<>();
        PRACTICE.pairs.put(PRACTICE, Arrays.asList(LOBBY_PRACTICE));
    }

    public static Server getByName(String name) {
        return Arrays.stream(values()).filter(server -> server.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public boolean canSend(Server server) {
        if (server == this) {
            return false;
        }

        return this.pairs.get(this).contains(server);
    }
}

