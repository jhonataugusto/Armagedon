package br.com.anticheat.menu;

import br.com.anticheat.AntiCheat;
import br.com.anticheat.check.api.manager.CheckManager;
import br.com.anticheat.gui.Button;
import br.com.anticheat.gui.Gui;
import br.com.anticheat.gui.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

public class MainMenu {
    public static void openMenu(Player opener) {

        Gui gui = new Gui(ChatColor.translateAlternateColorCodes('&', AntiCheat.getInstance().getFileManager().getGuiName()), 27);
        gui.addButton(new Button(1, 11, ItemUtil.makeItem(Material.PAPER, 1, "§b§lChecks", Arrays.asList(
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                "§7Manage check states",
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
        ))) {
            @Override
            public void onClick(Player clicker, ClickType clickType) {
                gui.close(clicker);
                ChecksMenu.openCheckSelectorMenu(clicker);
            }
        });

        gui.addButton(new Button(1, 13, ItemUtil.makeItem(Material.NETHER_STAR, 1, "§b§lInfo", Arrays.asList(
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                "§7Version: §a" + AntiCheat.getInstance().getBuild(),
                "§7TPS: §a" + (int) AntiCheat.getInstance().getTPS(),
                "§7RAM: §a" + (int) AntiCheat.getFreeMemory() + "§7/§a" + AntiCheat.getMaxMemory(),
                "§7",
                "§7Karhu has §b" + new CheckManager(AntiCheat.getInstance().getPlayerDataManager().getPlayerData(opener)).checkAmount() + " §7different checks",
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
        ))) {
            @Override
            public void onClick(Player clicker, ClickType clickType) {
                opener.updateInventory();
            }
        });

        gui.addButton(new Button(1, 15, ItemUtil.makeItem(Material.BOOK, 1, "§b§lSettings", Arrays.asList(
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
                "§7Control settings",
                "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
        ))) {
            @Override
            public void onClick(Player clicker, ClickType clickType) {
                gui.close(clicker);
                SettingsMenu.openSettingsMenu(clicker);
            }
        });
        gui.open(opener);
        opener.updateInventory();
    }

    private static String getBooleanValue3(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "%%__USER__%%";
    }

}
