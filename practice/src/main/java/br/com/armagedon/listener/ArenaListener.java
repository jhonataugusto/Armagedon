package br.com.armagedon.listener;

import br.com.armagedon.Practice;
import br.com.armagedon.arena.ArenaStage;
import br.com.armagedon.events.arena.ArenaChangeStateEvent;
import br.com.armagedon.events.user.UserDeathEvent;
import br.com.armagedon.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static br.com.armagedon.util.scheduler.SchedulerUtils.repeat;

public class ArenaListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageArenaEvent(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player || event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }

        User damager = User.fetch(event.getDamager().getUniqueId());
        User entity = User.fetch(event.getEntity().getUniqueId());

        if (damager == null || entity == null) {
            return;
        }

        if (damager.getArena() == null || entity.getArena() == null) {
            return;
        }

        if(damager.getArena().getStage() == ArenaStage.STARTING) {
            event.setCancelled(true);
            return;
        }

        if (event.getFinalDamage() >= entity.getPlayer().getHealth()) {
            event.setCancelled(true);
            Practice.getInstance().getServer().getPluginManager().callEvent(new UserDeathEvent(entity, damager, true));
        }
    }

    @EventHandler
    public void onPearlDamageEvent(EntityDamageEvent event) {

        if ((event.getCause() != EntityDamageEvent.DamageCause.FALL) && (event.getEntityType() != EntityType.ENDER_PEARL)) {
            return;
        }

        User entity = User.fetch(event.getEntity().getUniqueId());

        if (entity == null || entity.getArena() == null) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onArenaChangeState(ArenaChangeStateEvent event) {

        if (event.getArena() == null) {
            return;
        }

        switch (event.getStage()) {

            case STARTING:

                int duration = 5 * 20;
                int amplifier = 1;

                event.getArena().getAllTeamMembers().forEach(members -> {
                    members.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier, false, false));
                });

                repeat(new BukkitRunnable() {

                    int seconds = 5;

                    @Override
                    public void run() {
                        event.getArena().getAllTeamMembers().forEach(members -> {

                            if (seconds != 0) {
                                members.getPlayer().playSound(members.getPlayer().getLocation(), Sound.CLICK, 3.5F, 3.5F);
                                members.getPlayer().sendMessage("Iniciando o combate em " + seconds + " segundos.");
                            } else {
                                members.getPlayer().removePotionEffect(PotionEffectType.SLOW);
                                members.getPlayer().playSound(members.getPlayer().getLocation(), Sound.NOTE_PLING, 3F, 3F);
                                members.getPlayer().sendMessage("O duelo começou!");
                            }
                        });

                        if (seconds == 0) {

                            event.getArena().setStage(ArenaStage.PLAYING);

                            this.cancel();
                            return;
                        }

                        seconds--;
                    }
                }, 0, 20);
                break;

            case PLAYING:


                break;
            case ENDING:

                event.getArena().getAllTeamMembers().forEach(members -> {
                    event.getArena().getGame().handleQuit(members);
                });

                break;
        }
    }
}
