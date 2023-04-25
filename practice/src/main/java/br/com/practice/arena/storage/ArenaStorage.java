package br.com.practice.arena.storage;

import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import br.com.core.data.DuelContextData;
import br.com.practice.game.Game;
import br.com.practice.util.file.CompressionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

        getArenas().remove(arenaId);
        Bukkit.unloadWorld(arenaId, false);
        CompressionUtil.delete(arena.getMap().getDirectory());
    }

    public Arena getArena(String id) {
        return getArenas().getOrDefault(id, null);
    }

    public Map<String, Arena> getArenasByGame(Game game) {
        return getArenas().entrySet().stream().filter(entry -> entry.getValue().getGame().equals(game)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Arena getArenaByContextData(DuelContextData data) {
        return getArenas().values().stream().filter(value -> value.getData().equals(data)).findFirst().orElse(null);
    }

    public void loadMoreArenas(Game game) {
        int arenaSize = getArenas().size();
        int arenaSizeByFortyPercent = (int) Math.round(arenaSize * 0.4);

        for (int i = 0; i <= arenaSizeByFortyPercent; i++) {
            game.createArena(null);
        }
    }

    public Arena getFreeArena(Game game) {

        Arena arena = findFreeArena(game);

        if (arena == null) {
            loadMoreArenas(game);
            arena = findFreeArena(game);
        }

        return arena;
    }

    private Arena findFreeArena(Game game) {
        return getArenas().values().stream().filter(thisArena -> thisArena.getGame().equals(game) && thisArena.getStage() == ArenaStage.WAITING).findFirst().orElse(null);
    }
}
