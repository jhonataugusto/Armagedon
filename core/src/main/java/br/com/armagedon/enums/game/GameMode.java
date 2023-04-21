package br.com.armagedon.enums.game;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public enum GameMode {
    NODEBUFF("nodebuff", "NoDebuff"),
    SKYWARS("skywars", "SkyWars");
    private String name;
    private String displayName;

    GameMode(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    private static final GameMode[] values;

    static {
        values = values();
    }

    public static GameMode getByName(String name) {
        return Arrays.stream(values).filter(gamemode -> gamemode.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
