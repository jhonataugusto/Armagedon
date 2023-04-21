package br.com.armagedon.util.cuboid;

import br.com.armagedon.Core;
import br.com.armagedon.Practice;
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
    private double minX = 0, minY = 0, minZ = 0;
    private double maxX = 0, maxY = 0, maxZ = 0;
    private double team1X = 0, team1Y = 0, team1Z = 0, team1Pitch = 0, team1Yaw = 0;
    private double team2X = 0, team2Y = 0, team2Z = 0, team2Pitch = 0, team2Yaw = 0;

    public Cuboid() {

        if (!(isInside(team1X, team1Y, team1Z) || isInside(team2X, team2Y, team2Z))) {
            throw new RuntimeException("Os valores lidos não estão dentro do cuboid");
        }
    }

    public Location getTeamLocation1(World world) {
        return new Location(world, getTeam1X(), getTeam1Y(), getTeam1Z(), (float) getTeam1Pitch(), (float) getTeam1Yaw());
    }

    public Location getTeamLocation2(World world) {
        return new Location(world, getTeam2X(), getTeam2Y(), getTeam2Z(), (float) getTeam2Pitch(), (float) getTeam2Yaw());
    }

    public Cuboid(Location teamSpawn1, Location teamSpawn2) {
        this.teamSpawn1 = teamSpawn1;
        this.teamSpawn2 = teamSpawn2;
        minX = Math.min(teamSpawn1.getBlockX(), teamSpawn2.getBlockX());
        minY = Math.min(teamSpawn1.getBlockY(), teamSpawn2.getBlockY());
        minZ = Math.min(teamSpawn1.getBlockZ(), teamSpawn2.getBlockZ());
        maxX = Math.max(teamSpawn1.getBlockX(), teamSpawn2.getBlockX());
        maxY = Math.max(teamSpawn1.getBlockY(), teamSpawn2.getBlockY());
        maxZ = Math.max(teamSpawn1.getBlockZ(), teamSpawn2.getBlockZ());
    }

    public boolean isInside(Location location) {

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ);
    }

    public boolean isInside(double x, double y, double z) {
        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ);
    }

    public static Cuboid loadProperties(File directory) {
        Cuboid cuboid = new Cuboid();
        File file = new File(directory, "cuboid.json");

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                return cuboid = Core.GSON.fromJson(reader, Cuboid.class);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else {

            try (FileWriter writer = new FileWriter(file)) {
                file.createNewFile();
                writer.write(Core.GSON.toJson(cuboid));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return cuboid;
    }

    public Set<Chunk> getChunks(World world) {
        Set<Chunk> chunks = new HashSet<>();

        int x1 = (int) minX & ~0xf;
        int x2 = (int) maxX & ~0xf;
        int z1 = (int) minZ & ~0xf;
        int z2 = (int) maxZ & ~0xf;

        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                chunks.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }

        return chunks;
    }

}
