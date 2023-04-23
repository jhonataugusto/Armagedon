package br.com.armagedon.util.scheduler;

import br.com.armagedon.Practice;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public class SchedulerUtils {

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Practice.getInstance(), runnable);
    }

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(Practice.getInstance(), runnable);
    }

    public static void delay(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(Practice.getInstance(), runnable, delay);
    }

    public static void repeat(Runnable runnable, long initial_delay, long seconds_delay) {
        int tick = 20;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Practice.getInstance(), runnable, tick * initial_delay, tick * seconds_delay);
    }
}
