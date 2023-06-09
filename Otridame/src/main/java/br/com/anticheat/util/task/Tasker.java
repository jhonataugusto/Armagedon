package br.com.anticheat.util.task;

import br.com.anticheat.AntiCheat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class Tasker {

    public static void run(Runnable runnable) {
        AntiCheat.getInstance().getServer().getScheduler().runTask(AntiCheat.getInstance(), runnable);
    }

    private static BukkitTask taskTimer(Runnable runnable, Plugin plugin) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, 0L, 1L);
    }

    public static BukkitTask taskTimer(Runnable runnable) {
        return taskTimer(runnable, AntiCheat.getInstance());
    }

    private static BukkitTask taskAsync(Runnable runnable, Plugin plugin) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static BukkitTask taskAsync(Runnable runnable) {
        return taskAsync(runnable, AntiCheat.getInstance());
    }

    private static BukkitTask taskTimerAsync(Runnable runnable, Plugin plugin) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, 1L);
    }

    public static BukkitTask taskTimerAsync(Runnable runnable) {
        return taskTimerAsync(runnable, AntiCheat.getInstance());
    }

    public static void runSyncRepeating(Runnable runnable) {
        AntiCheat.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(AntiCheat.getInstance(), runnable, 1L, 1L);
    }

}
