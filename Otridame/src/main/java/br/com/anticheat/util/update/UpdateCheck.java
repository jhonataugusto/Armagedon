package br.com.anticheat.util.update;

import br.com.anticheat.AntiCheat;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateCheck {

    @Getter
    private static String newVersion;

    public static void checkVersion() {
        String licenseKey = AntiCheat.getInstance().getFileManager().getSettings().getString("License");

        try {
            newVersion = readLines();

            if (!AntiCheat.getInstance().getBuild().equals(newVersion)) {
                inform();
            }
        } catch (Exception ignored) {
        }
    }

    public static void inform() {
        Bukkit.getLogger().info("[Karhu] You are running an old version! Latest: " + newVersion);
    }

    public static void informStaff(Player player) {
        player.sendMessage("");
        player.sendMessage("§b§lKarhu §7┃ §f* §3§lUPDATE AVAILABLE §f* \n§fYour version: §c" + AntiCheat.getInstance().getBuild() + "\n§fLatest version: §b" + newVersion);
        player.sendMessage("");
    }

    public static String readLines() throws Exception {
        URLConnection connection = new URL("https://www.karhu.cc/paska.html").openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        return reader.readLine();
    }
}
