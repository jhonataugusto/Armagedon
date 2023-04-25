package br.com.practice.game;

import br.com.practice.Practice;
import br.com.core.account.Account;
import br.com.practice.arena.Arena;
import br.com.practice.arena.map.ArenaMap;
import br.com.core.data.DuelContextData;
import br.com.core.enums.game.GameMode;
import br.com.core.enums.map.Maps;
import br.com.core.enums.server.Server;
import br.com.practice.user.User;
import br.com.practice.util.bungee.BungeeUtils;
import br.com.practice.util.file.CompressionUtil;
import br.com.practice.util.world.VoidGenerator;
import br.com.practice.util.world.WorldHandler;
import br.com.practice.util.scheduler.SchedulerUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.Map;

import static br.com.practice.util.scheduler.SchedulerUtils.async;

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
            createArena(null);
        }
    }

    public void unload() {
        getPlugin().getArenaStorage().getArenas().forEach((key, value) -> CompressionUtil.delete(value.getMap().getDirectory()));
    }

    public void handleJoin(Player player) {
        User user = User.fetch(player.getUniqueId());

        if (user == null) {
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
        player.setSaturation(20);

        player.setGameMode(org.bukkit.GameMode.SURVIVAL);

        player.getActivePotionEffects().forEach(potionEffect -> {
            if(potionEffect.getType() != PotionEffectType.SLOW) {
                player.removePotionEffect(potionEffect.getType());
            }
        });
    }

    public void handleQuit(User user) {

        if (user == null) {
            return;
        }

        Player player = user.getPlayer();

        user.setArena(null);
        user.setLastDamager(null);

        BungeeUtils.connect(player, Server.LOBBY_PRACTICE);
    }

    public void handleScoreboard() {
    }

    public Arena createArena(DuelContextData data) {

        final Arena[] arena = new Arena[1];

        SchedulerUtils.sync(() -> {
            String mapName = data == null ? Maps.getRandomMap(getMode()).getName() : data.getMapName();
            ArenaMap map = new ArenaMap(mapName, getPresetMapDirectory(), getArenaDirectory());

            map.copyTo(map.getDirectory());

            WorldCreator creator = new WorldCreator(map.getDirectory().getPath());

            creator.generateStructures(false);
            creator.generator(VoidGenerator.getInstance());

            World world = Bukkit.getServer().createWorld(creator);

            WorldHandler.adjust(world, map.getArea().getChunks(world));

            arena[0] = new Arena(this, world, map);

            getPlugin().getArenaStorage().load(arena[0]);

            world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena[0]));

            File sessionLock = new File(arena[0].getMap().getDirectory(), "session.lock");

            if (!sessionLock.exists()) {
                plugin.getArenaStorage().unload(arena[0].getId());
                arena[0] = createArena(data);
            }

        });

        return arena[0];
    }
}
