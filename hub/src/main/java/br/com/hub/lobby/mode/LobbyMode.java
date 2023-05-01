package br.com.hub.lobby.mode;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public enum LobbyMode {

    MAIN("Main"),
    PRACTICE("Practice");

    private String name;

    LobbyMode(String name) {
        this.name = name;
    }

    @Getter
    private static final LobbyMode[] values;

    static {
        values = values();
    }

    public static LobbyMode getByName(String name) {
        return Arrays.stream(getValues()).filter(mode -> mode.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
