package br.com.practice.util.math;

import org.bukkit.Location;

public class MathUtils {
    public static double getEuclideanDistance(Location location1, Location location2){
        return location1.distance(location2);
    }
}
