package br.com.armagedon.util.cuboid;

import br.com.armagedon.Core;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.*;

@Getter
public class Cuboid {
    @Expose(serialize = false)
    private Location corner1, corner2;
    private int minX, minY, minZ;
    private int maxX, maxY, maxZ;

    public Cuboid(Location corner1, Location corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
    }

    public Cuboid(){}


    public boolean isInside(Location location) {

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return (x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ);
    }

    public static Cuboid loadProperties() {

        Cuboid cuboid = new Cuboid();

        File file = new File("cuboid.json");

        if(file.exists()) {
            try(FileReader reader = new FileReader(file)) {
                return cuboid = Core.GSON.fromJson(reader, Cuboid.class);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else {
            try(FileWriter writer = new FileWriter(file)){
                writer.write(Core.GSON.toJson(cuboid));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return null;
    }
}
