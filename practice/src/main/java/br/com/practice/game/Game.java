package br.com.practice.game;

import br.com.core.data.AccountData;
import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.arena.map.ArenaMap;
import br.com.core.data.DuelData;
import br.com.core.enums.game.GameMode;
import br.com.core.enums.map.Maps;
import br.com.core.enums.server.Server;
import br.com.practice.user.User;
import br.com.practice.util.bungee.BungeeUtils;
import br.com.practice.util.file.CompressionUtil;
import br.com.practice.util.serializer.SerializerUtils;
import br.com.practice.util.tag.TagUtil;
import br.com.practice.util.world.WorldHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

import static br.com.practice.util.scheduler.SchedulerUtils.*;

@Getter
@Setter
public abstract class Game implements Listener {
    private final GameMode mode;
    private final Practice plugin;
    private final Integer MIN_ARENAS;
    private final String presetMapDirectory;
    private final File arenaDirectory = new File("arenas");
    private boolean allowedBuild;

    public Game(Practice plugin, Integer minArenas, String presetMapDirectory) {
        this.mode = GameMode.getByName(this.getClass().getSimpleName());
        this.plugin = plugin;
        this.MIN_ARENAS = minArenas;
        this.presetMapDirectory = presetMapDirectory;
        this.allowedBuild = false;

        getPlugin().getServer().getPluginManager().registerEvents(this, Practice.getInstance());
    }

    public void load() {
        int actualArenas = plugin.getArenaStorage().getArenas().size();

        if (actualArenas != 0) {
            unload();
        }

        for (Maps map : Maps.getMapsFromMode(getMode())) {

            for (int i = 0; i <= MIN_ARENAS; i++) {

                createArena(map.getName());
            }
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

        player.setFireTicks(0);
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
            if (potionEffect.getType() != PotionEffectType.SLOW) {
                player.removePotionEffect(potionEffect.getType());
            }
        });

        user.getArena().handleScoreboard(user);

        TagUtil.loadTag(user, user.getTeam());

        user.getAccount().getData().setCurrentDuelUuid(user.getArena().getData().getUuid().toString());

        async(user.getAccount().getData()::saveData);
    }

    public void handleQuit(User user) {

        if (user == null || user.getPlayer() == null) {
            return;
        }

        TagUtil.unloadTag(user.getPlayer());

        user.setArena(null);
        user.setTeam(null);
        user.setLastDamager(null);

        if (user.getScoreboard() != null) {
            user.getScoreboard().removePlayer(user.getPlayer());
        }

        DuelData.removeDuelsFromAccount(user.getAccount());

        async(() -> BungeeUtils.connect(user.getPlayer(), Server.LOBBY_PRACTICE));
    }

    public void handleInventory(User user) {

        if (user.getArena() == null) {
            return;
        }

        AccountData data = user.getAccount().getData();
        GameMode mode = user.getArena().getGame().getMode();

        Inventory customInventory = null;
        if (data.getInventoryByGameModeName(mode.getName()) == null) {
            customInventory = SerializerUtils.deserializeInventory(mode.getDefaultInventoryEncoded(), user.getPlayer());
        } else {
            customInventory = SerializerUtils.deserializeInventory(data.getInventoryByGameModeName(mode.getName()).getInventoryEncoded(), user.getPlayer());
        }

        user.getPlayer().getInventory().setContents(customInventory.getContents());

    }

    public void handlePostMatchInventory(User user) {

        Inventory inventory = user.createPostMatchInventory();

        String base64 = SerializerUtils.serializeInventory(inventory);

        if (user.getArena() == null) {
            return;
        }

        Arena arena = user.getArena();

        arena.getData().getInventories().put(user.getAccount().getName() + DuelData.REGEX_NAME_UUID_SEPARATOR + user.getAccount().getUuid(), base64);
        arena.getData().saveData();

        user.setHits(0);
        user.setCriticalHits(0);
        user.setBlockedHits(0);
        user.setMaxCombo(0);
        user.setThrowedPotions(0);
        user.setSumAccuracyPotions(0);
        user.setAverageAccuracyPotions(0);
        user.setMaxClicksPerSecond(0);
        user.setRange(0);
        user.setSumOfRanges(0);
    }

    public void createArena(DuelData data) {
        sync(() -> {

            String mapName = data == null ? Maps.getRandomMap(getMode()).getName() : data.getMapName();

            ArenaMap map = new ArenaMap(mapName, getPresetMapDirectory(), getArenaDirectory());

            if (!map.copyTo(map.getDirectory())) {
                throw new RuntimeException("O mapa não foi copiado a tempo.");
            }

            WorldCreator creator = new WorldCreator(map.getDirectory().getPath());
            creator.generateStructures(false);
            World world = creator.createWorld();
            WorldHandler.adjust(world, map.getArea().getChunks(world));
            Arena arena = new Arena(this, world, map);
            getPlugin().getArenaStorage().load(arena);
            world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena));

            Practice.getInstance().getArenaStorage().getArenas().put(arena.getId(), arena);
        });
    }

    public void createArena(String mapName) {
        sync(() -> {
            ArenaMap map = new ArenaMap(mapName, getPresetMapDirectory(), getArenaDirectory());

            if (!map.copyTo(map.getDirectory())) {
                throw new RuntimeException("O mapa não foi copiado a tempo.");
            }

            WorldCreator creator = new WorldCreator(map.getDirectory().getPath());
            creator.generateStructures(false);
            World world = creator.createWorld();
            WorldHandler.adjust(world, map.getArea().getChunks(world));
            Arena arena = new Arena(this, world, map);
            getPlugin().getArenaStorage().load(arena);
            world.setMetadata("arena", new FixedMetadataValue(Practice.getInstance(), arena));

            Practice.getInstance().getArenaStorage().getArenas().put(arena.getId(), arena);
        });
    }
}
