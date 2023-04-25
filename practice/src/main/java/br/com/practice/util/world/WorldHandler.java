package br.com.practice.util.world;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;

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

        world.setSpawnLocation(0, 60, 0);
        world.setTime(6000);

        world.setAutoSave(false);


        //TODO: ele tá dando erro de session.lock, não sei muito bem como resolver, mas acredito que seja por conta do carregamento de chunks.
        //TODO: possível resolução: talvez deixar síncrono? ou fazer o Cuboid funcionar?

        for (Chunk chunk : chunks) {

            if (chunk.isLoaded()) {
                continue;
            }

            world.loadChunk(chunk.getX(), chunk.getZ(), true);
        }


        world.getEntities().forEach(Entity::remove);
    }
}
