package br.com.practice.game.list;

import br.com.core.utils.cooldown.CooldownManager;
import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.game.Game;
import br.com.practice.user.User;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static br.com.practice.util.scheduler.SchedulerUtils.async;
import static br.com.practice.util.scheduler.SchedulerUtils.delay;

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

    private final HashMap<UUID, Long> enderPearlCooldowns = new HashMap<>();
    private static final long PEARL_COOLDOWN_MILLIS = TimeUnit.SECONDS.toMillis(16);

    @EventHandler
    public void onEnderPearlThrowEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = User.fetch(player.getUniqueId());

        if (user == null || user.getArena() == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.ENDER_PEARL) {
            return;
        }

        if (enderPearlCooldowns.containsKey(user.getUuid())) {
            long timeLeft = enderPearlCooldowns.get(user.getUuid()) - System.currentTimeMillis();

            if (timeLeft > 0) {
                String formattedTime = DurationFormatUtils.formatDurationWords(timeLeft, true, true);
                player.sendMessage(ChatColor.RED + "Aguarde " + formattedTime + " para usar a enderpearl novamente.");
                event.setCancelled(true);
                return;
            }
        }

        enderPearlCooldowns.put(user.getUuid(), System.currentTimeMillis() + 16_000);

        new BukkitRunnable() {

            public void run() {
                long cooldownExpires = enderPearlCooldowns.getOrDefault(user.getUuid(), 0L);

                if (cooldownExpires < System.currentTimeMillis() || user.getArena() == null) {
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 3.5f, 3.5f);
                    player.sendMessage(ChatColor.GREEN + "Você pode usar sua enderpearl novamente.");
                    cancel();
                    return;
                }

                int millisLeft = (int) (cooldownExpires - System.currentTimeMillis());
                float percentLeft = (float) millisLeft / PEARL_COOLDOWN_MILLIS;

                user.getPlayer().setExp(percentLeft);
                user.getPlayer().setLevel(millisLeft / 1_000);
            }

        }.runTaskTimer(Practice.getInstance(), 1L, 1L);
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

    @EventHandler
    public void onPotionThrowEvent(PotionSplashEvent event) {

        ProjectileSource projectile = event.getEntity().getShooter();

        if (!(projectile instanceof Player)) {
            return;
        }

        Player player = (Player) projectile;

        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        if (user.getArena().getGame() != this) {
            return;
        }

        boolean missed = !event.getAffectedEntities().contains(player);

        if (missed) {
            user.setMissedPotions(user.getMissedPotions() + 1);
        } else {
            user.setSuccessfulPotions(user.getSuccessfulPotions() + 1);
            user.setSumAccuracyPotions(user.getSumAccuracyPotions() + event.getIntensity(user.getPlayer()));
        }

        event.getAffectedEntities().forEach(entity -> {

            boolean stealedPotion = !(entity.equals(player));

            if (!stealedPotion) {
                return;
            }

            User stealerUser = User.fetch(entity.getUniqueId());

            if (stealerUser == null) {
                return;
            }

            stealerUser.setStealedPotions(stealerUser.getStealedPotions() + 1);
        });

        user.setThrowedPotions(user.getThrowedPotions() + 1);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        Player player = event.getPlayer();

        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        if (user.getArena() == null) {
            return;
        }

        if (user.getArena().getGame() != this) {
            return;
        }

        delay(() -> {
            if (item.isValid()) item.remove();
        }, 3);
    }
}
