package br.com.armagedon.util.async;

import br.com.armagedon.Practice;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;
import java.util.function.Function;

public class AsyncUtils {

    public static void async(Consumer<Function<Void, Void>> consumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept((Void v) -> {
                    return null;
                });
            }
        }.runTaskAsynchronously(Practice.getInstance());
    }
}
