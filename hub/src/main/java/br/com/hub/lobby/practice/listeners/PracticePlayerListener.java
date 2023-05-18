package br.com.hub.lobby.practice.listeners;

import br.com.hub.Hub;
import br.com.hub.gui.preferences.PreferencesGUI;
import br.com.hub.gui.rank.LeaderboardsGUI;
import br.com.hub.gui.editor.ModeEditSelectorGUI;
import br.com.hub.gui.spectators.ActualDuelsGUI;
import br.com.hub.items.PracticeItems;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PracticePlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();

        for (PracticeItems item : PracticeItems.values()) {
            player.getInventory().setItem(item.getPosition(), item.toItemStack());
        }

        player.teleport(Hub.getInstance().getLobby().getSpawn());
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 5f, 5f);

        player.sendMessage(ChatColor.GREEN + "Agora você está no servidor de practice.");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.hasItem() || !event.getAction().name().contains("RIGHT_")) {
            return;
        }

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }

        Player player = event.getPlayer();

        NBT.modify(event.getItem(), nbt -> {
            if (nbt.hasCustomNbtData()) {

                if (nbt.getString(PracticeItems.KIT_EDITOR.getKEY()).equals(PracticeItems.KIT_EDITOR.getValue())) {
                    ModeEditSelectorGUI.INVENTORY.open(player);
                } else if (nbt.getString(PracticeItems.LEADERBOARD.getKEY()).equals(PracticeItems.LEADERBOARD.getValue())) {
                    LeaderboardsGUI.INVENTORY.open(player);
                } else if (nbt.getString(PracticeItems.CONFIGURATIONS.getKEY()).equals(PracticeItems.CONFIGURATIONS.getValue())) {
                    new PreferencesGUI().getINVENTORY().open(player);
                } else if (nbt.getString(PracticeItems.DUELS.getKEY()).equals(PracticeItems.DUELS.getValue())) {
                    ActualDuelsGUI.INVENTORY.open(player);
                }
            }
        });
    }
}
