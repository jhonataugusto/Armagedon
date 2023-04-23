package br.com.armagedon.game;

import br.com.armagedon.Practice;
import br.com.armagedon.account.Account;
import br.com.armagedon.arena.Arena;
import br.com.armagedon.arena.map.ArenaMap;
import br.com.armagedon.data.DuelContextData;
import br.com.armagedon.enums.game.GameMode;
import br.com.armagedon.enums.map.Maps;
import br.com.armagedon.enums.server.Server;
import br.com.armagedon.user.User;
import br.com.armagedon.util.bungee.BungeeUtils;
import br.com.armagedon.util.file.CompressionUtil;
import br.com.armagedon.util.world.WorldHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import java.io.File;

import static br.com.armagedon.util.scheduler.SchedulerUtils.delay;

@Getter
public abstract class Game implements Listener {
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

        getPlugin().getServer().getPluginManager().registerEvents(this, Practice.getInstance());
    }

    public void load() {
        int actualArenas = plugin.getArenaStorage().getArenas().size();

        if (actualArenas != 0) {
            unload();
        }

        int arenaSizedByFivePercent = (int) Math.round(MIN_ARENAS * 0.5);

        for (int i = 0; i <= arenaSizedByFivePercent; i++) {
            createArena();
        }
    }

    public void unload() {
        getPlugin().getArenaStorage().getArenas().forEach((key, value) -> CompressionUtil.delete(value.getMap().getDirectory()));
    }

    public void handleJoin(Player player) {
        User user = User.fetch(player.getUniqueId());

        if(user == null) {
            return;
        }

        Account account = user.getAccount();

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getOpenInventory().getTopInventory().clear();
        player.setLevel(0);
        player.setExp(0);
        player.setSaturation(14);
    }

    public void handleQuit(User user) {

        if(user == null) {
            return;
        }

        Player player = user.getPlayer();

        user.setArena(null);

        BungeeUtils.connect(player, Server.LOBBY_PRACTICE);
    }

    public void handleScoreboard() {
    }

    public Arena createArena(DuelContextData data) {

        String mapName = data.getMapName();
        ArenaMap map = new ArenaMap(mapName, getPresetMapDirectory(), getArenaDirectory());
        final Arena[] arena = new Arena[1];

        try {
            CompressionUtil.copy(new File(getPresetMapDirectory(), mapName), map.getDirectory(), null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        delay(() -> {

            WorldCreator creator = new WorldCreator(map.getDirectory().getPath());
            creator.generateStructures(false);

            World world = Bukkit.getServer().createWorld(creator);
            WorldHandler.adjust(world, map.getArea().getChunks(world));

            arena[0] = new Arena(this, world, map);

            world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena[0]));
            getPlugin().getArenaStorage().load(arena[0]);
        }, 1L);

        return arena[0];
    }

    public Arena createArena() {

        String mapName = Maps.getRandomMap(getMode()).getName();
        ArenaMap map = new ArenaMap(mapName, getPresetMapDirectory(), getArenaDirectory());
        final Arena[] arena = new Arena[1];

        try {
            CompressionUtil.copy(new File(getPresetMapDirectory(), mapName), map.getDirectory(), null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        delay(() -> {

            WorldCreator creator = new WorldCreator(map.getDirectory().getPath());
            creator.generateStructures(false);

            World world = Bukkit.getServer().createWorld(creator);
            WorldHandler.adjust(world, map.getArea().getChunks(world));

            arena[0] = new Arena(this, world, map);

            world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena[0]));
            getPlugin().getArenaStorage().load(arena[0]);
        }, 1L);

        return arena[0];
    }
}
