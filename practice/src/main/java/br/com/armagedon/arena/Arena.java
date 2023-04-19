package br.com.armagedon.arena;

import br.com.armagedon.arena.map.ArenaMap;
import br.com.armagedon.arena.team.ArenaTeam;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;

import java.util.UUID;
@Getter
@Setter
public class Arena {
    private String mode;
    private final String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0,6);
    private World world;
    private ArenaMap map;
    private ArenaTeam[] teams;

    public Arena(String mode, World world, ArenaMap arenaMap, ArenaTeam... teams) {
        this.mode = mode;
        this.world = world;
        this.map = arenaMap;
        this.teams = teams;
    }

    public String getDisplayArenaId() {
        return mode + "-" + id;
    }
}
