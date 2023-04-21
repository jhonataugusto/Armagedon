package br.com.armagedon.game;

import br.com.armagedon.Practice;
import br.com.armagedon.account.Account;
import br.com.armagedon.arena.Arena;
import br.com.armagedon.arena.map.ArenaMap;
import br.com.armagedon.data.DuelContextData;
import br.com.armagedon.enums.game.GameMode;
import br.com.armagedon.enums.map.Maps;
import br.com.armagedon.user.User;
import br.com.armagedon.util.file.CompressionUtil;
import br.com.armagedon.util.world.VoidGenerator;
import br.com.armagedon.util.world.WorldHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import java.io.File;

@Getter
public abstract class Game {
    private final GameMode mode;
    private final Practice plugin;
    private final Integer MIN_ARENAS;
    private final String mapDirectory;

    public Game(Practice plugin, Integer minArenas, String mapDirectory) {
        this.mode = GameMode.getByName(this.getClass().getSimpleName());
        this.plugin = plugin;
        this.MIN_ARENAS = minArenas;
        this.mapDirectory = mapDirectory;

        File file = new File(mapDirectory);

        if(!file.exists()) {
            file.mkdirs();
        }
    }

    public void load() {
        int actualArenas = plugin.getArenaStorage().getArenas().size();

        if (actualArenas != 0) {
            unload();
        }

        int arenaSizedByFivePercent = (int) Math.round(MIN_ARENAS * 0.5);

        for (int i = 0; i <= arenaSizedByFivePercent; i++) {
            handleArena();
        }
    }

    public void unload() {
        getPlugin().getArenaStorage().getArenas().forEach((key, value) -> CompressionUtil.delete(value.getMap().getFile()));
    }

    public void handleJoin(Player player) {
        User user = User.fetch(player.getUniqueId());

        Account account = user.getAccount();

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getOpenInventory().getTopInventory().clear();
        player.setLevel(0);
        player.setExp(0);
    }

    public void handleQuit(Player player) {

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setFireTicks(0);
    }

    public void handleScoreboard() {
    }

    public Arena handleArena(DuelContextData data) {

        //TODO: Verificar se o mapa está sendo carregado corretamente

        String mapName = data.getMapName();

        ArenaMap map = new ArenaMap(mapName, getMapDirectory());

        WorldCreator creator = new WorldCreator(map.getId());

        creator.generateStructures(false);
        //creator.generator(VoidGenerator.getInstance());

        World world = Bukkit.createWorld(creator);

        WorldHandler.adjust(world, map.getArea().getChunks(world));

        Arena arena = new Arena(this, world, map, data);

        //referencia de metadados, peguei do saturn, não sei pra que vou usar, mas posso usar pra algo mais pra frente
        world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena));

        getPlugin().getArenaStorage().load(arena);

        return arena;
    }

    public Arena handleArena() {

        //TODO: Verificar se o mapa está sendo carregado corretamente

        String mapName = Maps.getRandomMap(getMode()).getName();

        ArenaMap map = new ArenaMap(mapName, getMapDirectory());

        File arenaDirectory = new File("arenas", map.getId());

        if(!arenaDirectory.exists()) {
            arenaDirectory.mkdirs();
        }

        try {
            CompressionUtil.copy(new File(getMapDirectory(), mapName), arenaDirectory, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        WorldCreator creator = new WorldCreator(arenaDirectory.getAbsolutePath());

        creator.generateStructures(false);
        creator.generator(VoidGenerator.getInstance());

        World world = Bukkit.createWorld(creator);

        WorldHandler.adjust(world, map.getArea().getChunks(world));

        Arena arena = new Arena(this, world, map);

        //referencia de metadados, peguei do saturn, não sei pra que vou usar, mas posso usar pra algo mais pra frente
        world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena));

        getPlugin().getArenaStorage().load(arena);

        return arena;
    }
}
