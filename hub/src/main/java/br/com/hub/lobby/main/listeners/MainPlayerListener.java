package br.com.hub.lobby.main.listeners;

import br.com.core.Core;
import br.com.hub.Hub;
import br.com.hub.gui.ServerGUI;
import br.com.hub.items.LobbyItems;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MainPlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.hasItem() || !event.getAction().name().contains("RIGHT_")) {
            return;
        }

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }

        NBT.modify(event.getItem(), nbt -> {
            if (nbt.hasCustomNbtData()) {

                if (nbt.getString(LobbyItems.SERVER_LIST.getKey()).equals(LobbyItems.SERVER_LIST.getValue())) {
                    ServerGUI.INVENTORY.open(event.getPlayer());
                }
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        player.getInventory().clear();

        for (LobbyItems item : LobbyItems.values()) {
            event.getPlayer().getInventory().setItem(item.getPosition(), item.toItemStack());
        }

        player.teleport(Hub.getInstance().getLobby().getSpawn());
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 5, 5);

        player.sendMessage(ChatColor.RED + Core.SERVER_NAME + " " + Core.SERVER_VERSION);
        player.sendMessage("");
        player.sendMessage(ChatColor.AQUA + "Bem-vindo ao servidor de Network de Minecraft focado em PVP para 1.7/1.8!");
        player.sendMessage(ChatColor.YELLOW + "Estamos em beta, então sinta-se livre para reportar qualquer problema que encontrar.");
        player.sendMessage(ChatColor.BLUE + "Discord: " + Core.SERVER_DISCORD);
    }

}
