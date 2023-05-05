package br.com.practice.arena.storage;

import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import br.com.core.data.DuelData;
import br.com.practice.game.Game;
import br.com.practice.util.file.CompressionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static br.com.practice.util.scheduler.SchedulerUtils.sync;

@Getter
public class ArenaStorage {
    Map<String, Arena> arenas = new HashMap<>();

    public void load(Arena arena) {
        getArenas().put(arena.getId(), arena);
    }

    public void unload(String arenaId) {

        Arena arena = getArena(arenaId);

        if (arena == null) {
            return;
        }

        sync(() -> {
            getArenas().remove(arenaId);

            Bukkit.unloadWorld(arenaId, false);

            CompressionUtil.delete(arena.getMap().getDirectory());
        });
    }

    public Arena getArena(String id) {
        return getArenas().getOrDefault(id, null);
    }

    public Map<String, Arena> getArenasFromGame(Game game) {
        return getArenas().entrySet().stream().filter(entry -> entry.getValue().getGame().equals(game)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Arena getArenaByContextData(DuelData data) {
        return getArenas().values().stream().filter(value -> value.getData().equals(data)).findFirst().orElse(null);
    }

    public Arena getFreeArena(Game game, DuelData data) {

        Arena arena = findFreeArena(game, data.getMapName());

        int totalArenasFromGameMapSize = getArenasFromGameMap(game, data.getMapName()).size();
        int freeArenasSize = getFreeArenasFromGameMap(game, data.getMapName()).size();

        if (freeArenasSize <= totalArenasFromGameMapSize * 0.3) {

            int doubleFreeArena = freeArenasSize * 2;

            for (int i = 0; i <= doubleFreeArena; i++) {
                game.createArena(data);
            }

        }

        return arena;
    }

    private Arena findFreeArena(Game game) {
        return getArenas().values().stream().filter(thisArena -> thisArena.getGame().equals(game) && thisArena.getStage() == ArenaStage.WAITING).findFirst().orElse(null);
    }

    private Arena findFreeArena(Game game, String mapName) {
        return getArenas().values().stream().filter(thisArena -> thisArena.getGame().equals(game) && thisArena.getStage() == ArenaStage.WAITING && thisArena.getMap().getName().equalsIgnoreCase(mapName)).findFirst().orElse(null);
    }

    private Map<String, Arena> getArenasFromGameMap(Game game, String mapName) {
        return getArenas().values().stream()
                .filter(arena -> arena.getGame().equals(game) && arena.getMap().getName().equalsIgnoreCase(mapName))
                .collect(Collectors.toMap(Arena::getId, Function.identity()));
    }

    private Map<String, Arena> getFreeArenasFromGameMap(Game game, String mapName) {
        return getArenas().values().stream()
                .filter(arena -> arena.getStage().equals(ArenaStage.WAITING) && arena.getGame().equals(game) && arena.getMap().getName().equalsIgnoreCase(mapName))
                .collect(Collectors.toMap(Arena::getId, Function.identity()));
    }
}
