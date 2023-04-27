package br.com.practice.game.list.nodebuff;

import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.events.user.UserDeathEvent;
import br.com.practice.game.Game;
import br.com.practice.user.User;
import br.com.practice.util.serializer.SerializerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Nodebuff extends Game {

    public Nodebuff(Practice plugin, Integer min_rooms, String mapDirectory) {
        super(plugin, min_rooms, mapDirectory);
    }


    @Override
    public void handleJoin(Player player) {
        super.handleJoin(player);
    }

    @Override
    public void handleQuit(User user) {
        super.handleQuit(user);
    }

    @Override
    public void handleInventory(User user) {
        super.handleInventory(user);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);

        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        chestplate.addEnchantment(Enchantment.DURABILITY, 3);

        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        leggings.addEnchantment(Enchantment.DURABILITY, 3);

        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        boots.addEnchantment(Enchantment.DURABILITY, 3);

        user.getPlayer().getInventory().setHelmet(helmet);
        user.getPlayer().getInventory().setChestplate(chestplate);
        user.getPlayer().getInventory().setLeggings(leggings);
        user.getPlayer().getInventory().setBoots(boots);
    }

    @Override
    public void handleScoreboard() {
        super.handleScoreboard();
    }


    @EventHandler
    public void onPearlDamageEvent(EntityDamageEvent event) {

        if ((event.getCause() != EntityDamageEvent.DamageCause.FALL) || (event.getEntityType() != EntityType.ENDER_PEARL)) {
            return;
        }


        User entity = User.fetch(event.getEntity().getUniqueId());

        if (entity == null || entity.getArena() == null) {
            return;
        }

        event.setCancelled(true);
    }


    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        User entity = User.fetch(event.getEntity().getUniqueId());
        User damager = User.fetch(event.getDamager().getUniqueId());

        if (entity == null || damager == null) {
            return;
        }

        Arena arena = entity.getArena();

        if (arena == null || arena.getGame() != this) {
            return;
        }

        if (entity.getPlayer().isBlocking()) {
            entity.setBlockedHits(entity.getBlockedHits() + 1);
        }

        if (damager.getPlayer().getFallDistance() > 0) {
            damager.setCriticalHits(damager.getCriticalHits() + 1);
        }

        damager.setHits(damager.getHits() + 1);

        if (entity.getCurrentCombo() > 0) {
            if (entity.getCurrentCombo() > entity.getMaxCombo()) {
                entity.setMaxCombo(entity.getCurrentCombo());
            }
            entity.setCurrentCombo(0);
        }

        if (damager.getCurrentCombo() > 0) {
            if (damager.getCurrentCombo() > damager.getMaxCombo()) {
                damager.setMaxCombo(damager.getCurrentCombo());
            }
        }
        damager.setCurrentCombo(damager.getCurrentCombo() + 1);
    }
}
