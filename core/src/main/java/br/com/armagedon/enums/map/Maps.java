package br.com.armagedon.enums.map;

import br.com.armagedon.enums.game.GameMode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public enum Maps {
    GRASS("grass", "Grass", GameMode.NODEBUFF),
    STONE("stone", "Stone", GameMode.NODEBUFF),
    MUSHROOM("mushroom", "Mushroom",GameMode.SKYWARS),
    CASTLE("castle", "Castle", GameMode.SKYWARS);
    private String name;
    private String displayName;
    private GameMode mode;

    Maps(String name, String displayName, GameMode mode) {
        this.name = name;
        this.displayName = displayName;
        this.mode = mode;
    }

    private static final Maps[] values;

    static {
        values = values();
    }

    public static List<Maps> getMapsFromMode(GameMode mode) {
        return Arrays.stream(values).filter(map -> map.getMode().equals(mode)).collect(Collectors.toList());
    }

    public static Maps getMap(String name) {
        return Arrays.stream(values).filter(map -> map.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Maps getRandomMap(GameMode mode) {
        List<Maps> maps = getMapsFromMode(mode);

        Random random = new Random();

        int indexRandomized = random.nextInt(maps.size());

        return maps.get(indexRandomized);
    }
}
