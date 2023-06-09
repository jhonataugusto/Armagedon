package br.com.anticheat;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.Getter;
import br.com.anticheat.commands.ACCommand;
import br.com.anticheat.commands.sub.KarhuCommand;
import br.com.anticheat.handler.MovementProcessor;
import br.com.anticheat.handler.PacketProcessor;
import br.com.anticheat.listener.PlayerListener;
import br.com.anticheat.manager.AlertsManager;
import br.com.anticheat.manager.PlayerDataManager;
import br.com.anticheat.util.MathUtil;
import br.com.anticheat.util.command.CommandFramework;
import br.com.anticheat.util.file.FileManager;
import br.com.anticheat.util.update.UpdateCheck;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
PRIVILEGED's LICENSE KEY: 8POV-ISXY-XP8L-IPAN (i keep deleting it and spend 10m trying to find it)
 */

public class AntiCheat extends JavaPlugin {
    @Getter
    private static AntiCheat instance;
    @Getter
    private PlayerDataManager playerDataManager;
    @Getter
    private AlertsManager alertsManager;
    @Getter
    private CommandFramework framework;
    @Getter
    private FileManager fileManager;
    private double tps;
    private long serverTick;
    private long lastTickInMs;
    @Getter
    private Executor executorService;
    @Getter private ScheduledExecutorService transactionThread;
    @Getter public static ServerVersion SERVER_VERSION;
    @Getter
    private String build = "1.9.30";
    @Override
    public void onEnable() {
        instance = this;
        this.fileManager = new FileManager(this);
        PacketEvents.create(this);
//        PacketEvents.get().getSettings().backupServerVersion(ServerVersion.v_1_8_3);
        PacketEvents.getAPI().getSettings().injectAsync(true);
        PacketEvents.get().getSettings().ejectAsync(true);
        PacketEvents.get().getSettings().checkForUpdates(false);
        PacketEvents.get().init(this);
        this.executorService = Executors.newSingleThreadExecutor();
        this.transactionThread = Executors.newSingleThreadScheduledExecutor();
        this.framework = new CommandFramework(this);
        this.alertsManager = new AlertsManager();
        this.playerDataManager = new PlayerDataManager(this);
        this.registerCommands();
        SERVER_VERSION = PacketEvents.getAPI().getServerUtils().getVersion();
        //this.DeleteOldFiles();

        PacketEvents.getAPI().getEventManager().registerListener(new PacketProcessor());
        PacketEvents.getAPI().getEventManager().registerListener(new MovementProcessor());

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        UpdateCheck.checkVersion();

        handleTick();

        runTasks();

    }
    @Override
    public void onDisable() {
        PacketEvents.get().stop();
        this.playerDataManager.getPlayerDataMap().clear();
    }

    public void DeleteOldFiles() {
        File file = new File(this.getDataFolder() + "/logs");
        if (file.isDirectory()) {
            for (File files : Objects.requireNonNull(file.listFiles())) {
                long diff = new Date().getTime() - files.lastModified();
                if (diff > 24 * 24 * 60 * 60 * 1000) {
                    files.deleteOnExit();
                }
            }
        }
    }

    private void registerCommands() {
        new ACCommand();
        new KarhuCommand();
    }

    public void handleTick() {
        if (!this.transactionThread.isShutdown()) {
            this.transactionThread.scheduleAtFixedRate(() -> {
                if (this.serverTick % 5 == 0) {
                    sendTransactions();
                }
            }, 50L, 50L, TimeUnit.MILLISECONDS);
        }
    }

    public void sendTransactions() {
        playerDataManager.getPlayerDataMap().values().forEach(data -> {
            if (data != null) {
                ++data.timerTransactionSent;
                if (data.timerTransactionSent > 0) {
                    data.timerTransactionSent = Short.MIN_VALUE;
                }
                data.transactionTime.put(data.timerTransactionSent, System.currentTimeMillis());
                PacketEvents.getAPI().getPlayerUtils().sendPacket(data.getDataPlayer(), new WrappedPacketOutTransaction(0, data.timerTransactionSent, false));
            }
        });
    }
    public void runTasks() {

        new BukkitRunnable() {

            long sec;
            long currentSec;
            int ticks;

            public void run() {

                if (serverTick == Long.MAX_VALUE) {
                    serverTick = 0;
                }

                ++serverTick;
                long ms = System.currentTimeMillis();
                sec = (System.currentTimeMillis() / 1000L);
                if (currentSec == sec) {
                    ticks++;
                } else {
                    currentSec = sec;
                    tps = (tps == 0.0D ? ticks : (tps + ticks) / 2.0D);
                    ticks = 1;
                }

                lastTickInMs = ms;
            }

        }.runTaskTimer(this, 0L, 1L);

    }

    public double getTPS() {
        return Math.min(tps + 1.0, 20.0);
    }

    public double getLag() {
        return MathUtil.round((1.0 - tps / 20.0) * 100.0);
    }

    public boolean isServerLagging() {
        return tps < 18 || (System.currentTimeMillis() - lastTickInMs) > 100;
    }

    public static long getFreeMemory() {
        Runtime r = Runtime.getRuntime();
        return r.freeMemory() / 1024L / 1024L;
    }

    public static long getMaxMemory() {
        Runtime r = Runtime.getRuntime();
        return r.maxMemory() / 1024L / 1024L;
    }

    public static long getTotalMemory() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() / 1024L / 1024L;
    }
}
