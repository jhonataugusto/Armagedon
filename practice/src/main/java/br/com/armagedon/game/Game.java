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
    private final String presetMapDirectory;
    private final File arenaDirectory = new File("arenas");

    public Game(Practice plugin, Integer minArenas, String presetMapDirectory) {
        this.mode = GameMode.getByName(this.getClass().getSimpleName());
        this.plugin = plugin;
        this.MIN_ARENAS = minArenas;
        this.presetMapDirectory = presetMapDirectory;
    }

    public void load() {
        int actualArenas = plugin.getArenaStorage().getArenas().size();

        if (actualArenas != 0) {
            unload(); //TODO: esse unload apaga os arquivos do nodebuff
        }

        int arenaSizedByFivePercent = (int) Math.round(MIN_ARENAS * 0.5);

        for (int i = 0; i <= arenaSizedByFivePercent; i++) {
            handleArena();
        }
    }

    public void unload() {
        getPlugin().getArenaStorage().getArenas().forEach((key, value) -> CompressionUtil.delete(value.getMap().getDirectory()));
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

        String mapName = data.getMapName();
        ArenaMap map = new ArenaMap(mapName, getPresetMapDirectory(), getArenaDirectory());

        WorldCreator creator = new WorldCreator(map.getDirectory().getPath());
        creator.generateStructures(false);

        try {
            CompressionUtil.copy(new File(getPresetMapDirectory(), mapName), map.getDirectory(), null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        World world = Bukkit.createWorld(creator);
        WorldHandler.adjust(world, map.getArea().getChunks(world));

        Arena arena = new Arena(this, world, map);

        world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena));

        getPlugin().getArenaStorage().load(arena);

        return arena;
    }

    public Arena handleArena() {

        String mapName = Maps.getRandomMap(getMode()).getName();
        ArenaMap map = new ArenaMap(mapName, getPresetMapDirectory(), getArenaDirectory());

        WorldCreator creator = new WorldCreator(map.getDirectory().getPath());
        creator.generateStructures(false);

        try {
            CompressionUtil.copy(new File(getPresetMapDirectory(), mapName), map.getDirectory(), null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        World world = Bukkit.createWorld(creator);
        WorldHandler.adjust(world, map.getArea().getChunks(world));

        Arena arena = new Arena(this, world, map);

        world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena));

        getPlugin().getArenaStorage().load(arena);

        return arena;
    }
}
