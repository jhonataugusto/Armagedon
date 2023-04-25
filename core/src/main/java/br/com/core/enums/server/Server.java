package br.com.core.enums.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Server {
    LOBBY("lobby"),
    LOBBY_PRACTICE("lobby_practice"),
    PRACTICE("practice");

    private String name;


    public static Server getByName(String name) {
        return Arrays.stream(values()).filter(server -> server.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
