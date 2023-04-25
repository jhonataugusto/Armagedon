package br.com.hub.util.scheduler;

import br.com.hub.Hub;
import org.bukkit.Bukkit;

public class SchedulerUtils {

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), runnable);
    }

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(Hub.getInstance(), runnable);
    }

    public static void delay(Runnable runnable, long seconds_delay) {
        int tick = 20;
        Bukkit.getScheduler().runTaskLater(Hub.getInstance(), runnable, tick * seconds_delay);
    }

    public static void repeat(Runnable runnable, long initial_delay, long seconds_delay) {
        int tick = 20;
        Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), runnable, tick * initial_delay, tick * seconds_delay);

    }
}
