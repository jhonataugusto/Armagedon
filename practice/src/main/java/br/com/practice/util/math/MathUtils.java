package br.com.practice.util.math;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class MathUtils {


    public static double getEuclideanDistance(Player player, Player target) {

        double range = player.getEyeLocation().toVector().distance(target.getEyeLocation().toVector()) - 0.41;
        return trim(2, range);

        //0.41 - no sprinting
    }

    public static double trim(int decimalPlaces, double value) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(value * multiplier) / multiplier;
    }

}
