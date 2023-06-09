package br.com.anticheat.menu;

import br.com.anticheat.AntiCheat;
import br.com.anticheat.gui.Button;
import br.com.anticheat.gui.Gui;
import br.com.anticheat.gui.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

public class SettingsMenu {


    private static String getBooleanValue(boolean value) {
        return value ? "§aOn" : "§cOff";
    }

    private String getBooleanValue2(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "§cOff";
    }

    public static boolean getAutoban() {
        return AntiCheat.getInstance().getFileManager().isAutoban();
    }
    public static boolean getPullback() {
        return AntiCheat.getInstance().getFileManager().isPullback();
    }

    public static void openSettingsMenu(Player opener) {

        final boolean pullback = getPullback();
        final boolean autoban = getAutoban();

        Gui gui = new Gui("Settings", 27);

        gui.addButton(new Button(1, 12, ItemUtil.makeItem((autoban) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§bAutoban", Arrays.asList(
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                "§7Autoban: " + getBooleanValue(autoban),
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
        ))) {
            @Override
            public void onClick(Player clicker, ClickType clickType) {
                gui.close(clicker);
                if (autoban) {
                    AntiCheat.getInstance().getFileManager().getSettings().set("autoban", false);
                    clicker.sendMessage("§b§lKARHU §7// §cAutoban off!");
                } else {
                    AntiCheat.getInstance().getFileManager().getSettings().set("autoban", true);
                    clicker.sendMessage("§b§lKARHU §7// §aAutoban on!");
                    opener.updateInventory();
                }
                AntiCheat.getInstance().getFileManager().save();
                AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                openSettingsMenu(clicker);
            }
        });

        gui.addButton(new Button(1, 14, ItemUtil.makeItem((pullback) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, 1, "§bPullback", Arrays.asList(
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                "§7Pullback: " + getBooleanValue(pullback),
                "§7This ensures players can't use movement modules",
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
        ))) {
            @Override
            public void onClick(Player clicker, ClickType clickType) {
                gui.close(clicker);
                if (pullback) {
                    AntiCheat.getInstance().getFileManager().getSettings().set("pullback", false);
                    clicker.sendMessage("§b§lKARHU §7// §cPullback off!");
                } else {
                    AntiCheat.getInstance().getFileManager().getSettings().set("pullback", true);
                    clicker.sendMessage("§b§lKARHU §7// §aPullback on!");
                    opener.updateInventory();
                }
                AntiCheat.getInstance().getFileManager().save();
                AntiCheat.getInstance().getFileManager().load(AntiCheat.getInstance());
                openSettingsMenu(clicker);
            }
        });

        gui.addButton(new Button(1, 26, ItemUtil.makeItem(Material.ARROW, 1, "§b§lReturn", Arrays.asList(
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                "§7Return to main page",
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
        ))) {
            @Override
            public void onClick(Player clicker, ClickType clickType) {
                gui.close(clicker);
                MainMenu.openMenu(clicker);
            }
        });
        gui.open(opener);
        opener.updateInventory();
    }
    private String getBooleanValue3(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "%%__USER__%%";
    }
}
