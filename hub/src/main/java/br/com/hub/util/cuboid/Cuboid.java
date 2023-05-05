package br.com.hub.util.cuboid;

import br.com.core.Core;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Cuboid {
    private transient Location teamSpawn1, teamSpawn2;
    private double position1X, position1Y, position1Z;
    private double spawnX, spawnY, spawnZ, spawnPitch, spawnYaw;
    private double position2X, position2Y, position2Z;
    private transient double minX, minY, minZ, maxX, maxY, maxZ;

    public Cuboid() {

    }

    public Location getCenter(World world) {
        double centerX = minX + maxX / 2;
        double centerY = minY + maxY / 2;
        double centerZ = minZ + maxZ / 2;
        return new Location(world, centerX, centerY, centerZ);
    }

    public boolean isInside(Location location) {

        int locationX = location.getBlockX();
        int locationY = location.getBlockY();
        int locationZ = location.getBlockZ();

        setMinX(Math.min(position1X, position2X));
        setMinY(Math.min(position1Y, position2Y));
        setMinZ(Math.min(position1Z, position2Z));

        setMaxX(Math.max(position1X, position2X));
        setMaxY(Math.max(position1Y, position2Y));
        setMaxZ(Math.max(position1Z, position2Z));

        return isInside(locationX, locationY, locationZ);
    }

    public boolean isInside(double x, double y, double z) {

        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ);
    }

    public static Cuboid loadProperties(File directory) {

        Cuboid area = new Cuboid();

        File children = new File(directory, "cuboid.json");

        if (children.exists()) {

            try (FileReader reader = new FileReader(children)) {

                return Core.GSON.fromJson(reader, Cuboid.class);

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else {

            try (FileWriter writer = new FileWriter(children)) {

                children.createNewFile();

                writer.write(Core.GSON.toJson(area));

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return area;
    }

    public Set<Chunk> getChunks(World world) {
        Set<Chunk> chunks = new HashSet<>();

        int x1 = (int) position1X & ~0xf;
        int x2 = (int) position2X & ~0xf;
        int z1 = (int) position1Z & ~0xf;
        int z2 = (int) position2Z & ~0xf;

        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                chunks.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }

        return chunks;
    }

}