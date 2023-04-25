package br.com.practice.util.scheduler;

import br.com.practice.Practice;
import org.bukkit.Bukkit;

public class SchedulerUtils {

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Practice.getInstance(), runnable);
    }

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(Practice.getInstance(), runnable);
    }

    public static void delay(Runnable runnable, long seconds_delay) {
        int tick = 20;
        Bukkit.getScheduler().runTaskLater(Practice.getInstance(), runnable, tick * seconds_delay);
    }

    public static void repeat(Runnable runnable, long initial_delay, long seconds_delay) {
        int tick = 20;
        Bukkit.getScheduler().runTaskTimer(Practice.getInstance(), runnable, tick * initial_delay, tick * seconds_delay);

    }
}
