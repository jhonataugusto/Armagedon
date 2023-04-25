package br.com.practice.game.list.nodebuff;

import br.com.practice.Practice;
import br.com.practice.game.Game;
import br.com.practice.user.User;
import br.com.practice.util.serializer.SerializerUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
    }

    @Override
    public void handleQuit(User user) {
        super.handleQuit(user);
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
    public void onDamageFireTickEvent(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {

        }
    }
}
