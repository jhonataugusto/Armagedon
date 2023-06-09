package br.com.bungee.util.scheduler;

import br.com.bungee.Bungee;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.concurrent.TimeUnit;

public class SchedulerUtils {


    public static void async(Runnable runnable) {
        TaskScheduler scheduler = Bungee.getInstance().getProxy().getScheduler();
        scheduler.runAsync(Bungee.getInstance(), runnable);
    }

    public static void delay(Runnable runnable, long secondsDelay) {
        TaskScheduler scheduler = Bungee.getInstance().getProxy().getScheduler();
        long tickDelay = TimeUnit.SECONDS.toMillis(secondsDelay) / 50;
        scheduler.schedule(Bungee.getInstance(), runnable, tickDelay, TimeUnit.MILLISECONDS);
    }

    public static void repeat(Runnable runnable, long initialDelay, long secondsRepeat) {
        TaskScheduler scheduler = Bungee.getInstance().getProxy().getScheduler();
        long tickInitialDelay = TimeUnit.SECONDS.toMillis(initialDelay) / 50;
        long tickRepeat = TimeUnit.SECONDS.toMillis(secondsRepeat) / 50;
        scheduler.schedule(Bungee.getInstance(), runnable, tickInitialDelay, tickRepeat, TimeUnit.MILLISECONDS);
    }
}
