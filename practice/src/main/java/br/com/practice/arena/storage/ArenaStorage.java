package br.com.practice.arena.storage;

import br.com.core.enums.game.GameMode;
import br.com.core.enums.map.Maps;
import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import br.com.core.data.DuelData;
import br.com.practice.game.Game;
import br.com.practice.util.file.CompressionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static br.com.practice.util.scheduler.SchedulerUtils.*;

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

        Bukkit.unloadWorld(arenaId, false);

        sync(() -> {
            getArenas().remove(arenaId);
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

        int totalArenas = getArenasFromGameMap(game, data.getMapName()).size();
        int freeArenas = getFreeArenasFromGameMap(game, data.getMapName()).size();

        delay(() -> {

            if (freeArenas <= totalArenas * 0.5) {

                int freeArenasMultiplier = freeArenas * 2;

                int i = 0;
                while (i <= freeArenasMultiplier) {
                    game.createArena(data);
                    i++;
                }

                System.out.println("Foram carregadas " + i + " novas arenas do mapa " + data.getMapName());
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.YELLOW + "\nNovas arenas estão sendo geradas! O servidor pode sofrer um pequeno lag, mas voltará ao normal em breve. Obrigado pela compreensão!\n"));
            }

        }, 10);


        return arena;
    }

    public void unloadUnusedArenaMaps() {
        async(() -> {
            for (Maps map : Maps.values()) {
                GameMode mode = map.getMode();
                Game game = Practice.getInstance().getGameStorage().getGame(mode);

                Map<String, Arena> freeArenas = getFreeArenasFromGameMap(game, map.getName());
                Map<String, Arena> totalArenas = getArenasFromGameMap(game, map.getName());

                int freeArenasSize = freeArenas.size();
                int totalArenasSize = totalArenas.size();

                if (freeArenasSize >= totalArenasSize / 2 && freeArenasSize > 0) {
                    int reducedFreeArenasSize = freeArenasSize / 2;

                    int i = 0;
                    Iterator<Arena> iterator = freeArenas.values().iterator();
                    while (iterator.hasNext() && i < reducedFreeArenasSize) {
                        Arena arena = iterator.next();
                        sync(() -> {
                            unload(arena.getId());
                        });
                        iterator.remove();
                        i++;
                    }

                    System.out.println("Foram descarregadas " + i + " arenas do mapa" + map.getDisplayName());
                }
            }
        });
    }

    private Arena findFreeArena(Game game) {
        return getArenas().values().stream().filter(thisArena -> thisArena.getGame().equals(game) && thisArena.getStage() == ArenaStage.FREE).findFirst().orElse(null);
    }

    private Arena findFreeArena(Game game, String mapName) {
        return getArenas().values().stream().filter(thisArena ->
                thisArena.getGame().equals(game)
                        && thisArena.getStage() == ArenaStage.FREE
                        && thisArena.getMap().getName().equalsIgnoreCase(mapName)).findFirst().orElse(null);
    }

    private Map<String, Arena> getArenasFromGameMap(Game game, String mapName) {
        return getArenas().values().stream()
                .filter(arena -> arena.getGame().equals(game) && arena.getMap().getName().equalsIgnoreCase(mapName))
                .collect(Collectors.toMap(Arena::getId, Function.identity()));
    }

    private Map<String, Arena> getFreeArenasFromGameMap(Game game, String mapName) {
        return getArenas().values().stream()
                .filter(arena -> arena.getStage().equals(ArenaStage.FREE) && arena.getGame().equals(game) && arena.getMap().getName().equalsIgnoreCase(mapName))
                .collect(Collectors.toMap(Arena::getId, Function.identity()));
    }
}
