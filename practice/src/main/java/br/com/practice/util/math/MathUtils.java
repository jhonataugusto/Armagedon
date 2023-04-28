package br.com.practice.util.math;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class MathUtils {
    public static double getEuclideanDistance(Player player, Player target) {
        return player.getEyeLocation().distance(target.getEyeLocation());
    }
}
