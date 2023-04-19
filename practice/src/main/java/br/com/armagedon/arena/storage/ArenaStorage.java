package br.com.armagedon.arena.storage;

import br.com.armagedon.arena.Arena;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ArenaStorage {
    Map<String, Arena> arenas = new HashMap<>();

    public void register(Arena arena) {
        getArenas().put(arena.getId(), arena);
    }

    public void remove(String arenaId) {
        getArenas().remove(arenaId);
    }

    public Arena getArena(String id) {
        return getArenas().getOrDefault(id,null);
    }


}
