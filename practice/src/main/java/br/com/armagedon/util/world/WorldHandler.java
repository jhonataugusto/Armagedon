package br.com.armagedon.util.world;

import br.com.armagedon.Practice;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class WorldHandler {
    public static void adjust(World world, Set<Chunk> chunks) {

        world.setPVP(true);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("naturalRegeneration", "false");
        world.setGameRuleValue("sendCommandFeedback", "false");
        world.setGameRuleValue("logAdminCommands", "false");

        world.setStorm(false);
        world.setThundering(false);
        world.setWeatherDuration(Integer.MIN_VALUE);
        world.setThunderDuration(Integer.MIN_VALUE);

        world.setSpawnLocation(0, 71, 0);
        world.setTime(6000);

        world.setAutoSave(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Chunk chunk : chunks) {

                    if (chunk.isLoaded()) {
                        continue;
                    }

                    world.loadChunk(chunk.getX(), chunk.getZ(), true);
                }
            }
        }.runTaskAsynchronously(Practice.getInstance());


        world.getEntities().forEach(Entity::remove);
    }
}
