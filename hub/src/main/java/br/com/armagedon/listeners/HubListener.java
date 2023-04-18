package br.com.armagedon.listeners;

import br.com.armagedon.Hub;

import br.com.armagedon.gui.ServerGUI;
import br.com.armagedon.items.LobbyItems;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class HubListener implements Listener {


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setLevel(0);
        player.setExp(0);
        player.setSaturation(14);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        PlayerInventory playerInventory = player.getInventory();

        playerInventory.clear();
        playerInventory.setArmorContents(null);
    }

//    @EventHandler
//    public void onDefineSpawn(PlayerInitialSpawnEvent event) {
//        //TODO: fazer o rapaz spawnar em algum lugar depois
//    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SLIME_BLOCK) {
            Vector vector = Hub.getInstance().getLobby().getSpawn().clone().getDirection().multiply(3).setY(0.66);

            Player player = event.getPlayer();

            player.setVelocity(vector);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.5F, 2.5F);
        }
    }
}
