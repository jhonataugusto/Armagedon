package br.com.anticheat.listener;

import br.com.anticheat.AntiCheat;
import br.com.anticheat.util.update.UpdateCheck;
import br.com.anticheat.data.PlayerData;
import br.com.anticheat.gui.Button;
import br.com.anticheat.gui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.concurrent.ForkJoinPool;

public class PlayerListener implements Listener {
    private final AntiCheat plugin;

    @EventHandler
    public void onVehicle(VehicleEnterEvent e) {

        if(e.getEntered() instanceof Player) {

            final Player player = (Player) e.getEntered();

            final PlayerData playerData = AntiCheat.getInstance().getPlayerDataManager().getPlayerData(player);

            playerData.setLastVehicleTicks(playerData.getTotalTicks());
        }


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {

        final Player player = e.getPlayer();

        ForkJoinPool.commonPool().execute(() -> AntiCheat.getInstance().getPlayerDataManager().add(e.getPlayer()));

        if (player.hasPermission("karhu.alerts")) {
            AntiCheat.getInstance().getAlertsManager().toggleAlerts(player);
        }


        if(UpdateCheck.getNewVersion() != null) {
            if (!AntiCheat.getInstance().getBuild().equals(UpdateCheck.getNewVersion())) {
                if(player.hasPermission("karhu.staff")) UpdateCheck.informStaff(player);
            }
        }
    }

    private String dogshit24(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "%%__USER__%%";
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        if (AntiCheat.getInstance().getAlertsManager().hasAlertsToggled(e.getPlayer())) {
            AntiCheat.getInstance().getAlertsManager().toggleAlerts(e.getPlayer());
        }

        ForkJoinPool.commonPool().execute(() -> AntiCheat.getInstance().getPlayerDataManager().remove(e.getPlayer()));
    }

    private String dogshit13(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "%%__USER__%%";
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        final PlayerData playerData = AntiCheat.getInstance().getPlayerDataManager().getPlayerData(event.getPlayer());
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock() != null
                && !event.isCancelled()
                && (event.getClickedBlock().getType().name().contains("DOOR") || event.getClickedBlock().getType().name().contains("FENCE"))) {
            playerData.setLastOpeningInteract(playerData.getTotalTicks());
        }
    }


    public PlayerListener(AntiCheat plugin) {
        this.plugin = plugin;
    }

    private String dogshit11(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "%%__USER__%%";
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if(e.getCurrentItem() == null) return;
        if(e.getWhoClicked().getOpenInventory().getTitle().contains("§r")) e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        if(Gui.getGui(player) != null) {
            Gui gui = Gui.getGui(player);
            if(e.getCurrentItem() != null) {
                for(Button b : gui.getButtons()) {
                    if(b.item.clone().equals(e.getCurrentItem())) {
                        e.setCancelled(true);
                        b.onClick(player, e.getClick());
                    }
                }
            }
        }
    }

    private String dogshi1(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "%%__USER__%%";
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInvClose(InventoryCloseEvent e) {

        Player player = (Player) e.getPlayer();
        if(e.getView().getTitle().contains("§r") && Gui.getGui(player) != null) {
            Gui.getGui(player).close(player);
        }

    }

    private String dogshit(boolean value) {
        return value ? "%%__TIMESTAMP__%%" : "%%__USER__%%";
    }
}
