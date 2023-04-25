package br.com.practice.listener;

import br.com.core.data.AccountData;
import br.com.core.enums.game.GameMode;
import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import br.com.practice.events.arena.ArenaChangeStateEvent;
import br.com.practice.events.arena.ArenaEndEvent;
import br.com.practice.events.arena.ArenaStartEvent;
import br.com.practice.events.user.UserExitArenaBoundsEvent;
import br.com.practice.events.user.UserDeathEvent;
import br.com.practice.user.User;
import br.com.practice.util.cuboid.Cuboid;
import br.com.practice.util.scheduler.SchedulerUtils;
import br.com.practice.util.serializer.SerializerUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static br.com.practice.util.scheduler.SchedulerUtils.delay;
import static br.com.practice.util.scheduler.SchedulerUtils.sync;

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

        if (damager.getArena().getStage() == ArenaStage.STARTING || damager.getArena().getStage() == ArenaStage.ENDING) {
            event.setCancelled(true);
            return;
        }

        if (entity.getLastDamager() == null) {
            entity.setLastDamager(damager);
        }

        if (event.getFinalDamage() >= entity.getPlayer().getHealth()) {

            event.setCancelled(true);

            if(event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                Practice.getInstance().getServer().getPluginManager().callEvent(new UserDeathEvent(entity.getArena(), entity, null, entity.lastMember()));
            } else {
                Practice.getInstance().getServer().getPluginManager().callEvent(new UserDeathEvent(entity.getArena(), entity, damager, entity.lastMember()));
            }
        }
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

                new BukkitRunnable() {
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
                            Bukkit.getServer().getPluginManager().callEvent(new ArenaStartEvent(event.getArena()));

                            this.cancel();
                            Bukkit.getScheduler().cancelTask(this.getTaskId());
                            return;
                        }

                        seconds--;
                    }
                }.runTaskTimer(Practice.getInstance(), 0, 20);
                break;

            case ENDING:
                Bukkit.getServer().getPluginManager().callEvent(new ArenaEndEvent(event.getArena()));
                break;
        }
    }

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        User user = User.fetch(player.getUniqueId());

        if (user == null) {
            return;
        }

        Location currentLocation = player.getLocation();

        if (user.getArena() == null) {
            return;
        }

        Arena arena = user.getArena();
        Cuboid bounds = arena.getMap().getArea();

        if (!user.getArena().getMap().getArea().isInside(currentLocation)) {
            Bukkit.getServer().getPluginManager().callEvent(new UserExitArenaBoundsEvent(user, arena, bounds));
        }
    }

    @EventHandler
    public void onUserExitBounds(UserExitArenaBoundsEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(new UserDeathEvent(event.getArena(), event.getUser(), event.getUser().getLastDamager(), event.getUser().lastMember()));

        event.getUser().getPlayer().teleport(event.getArena().getMap().getArea().getCenter(event.getArena().getWorld()));
        event.getUser().getPlayer().sendMessage("Saiu da arena");
    }


    @EventHandler
    public void onArenaStart(ArenaStartEvent event) {

        event.getArena().getTeams().forEach(team -> {
            team.getMembers().forEach(members -> {

                AccountData data = members.getAccount().getData();
                GameMode mode = event.getArena().getGame().getMode();

                Inventory customInventory = null;
                if (!data.getInventories().containsKey(mode.getName())) {
                    customInventory = SerializerUtils.deserializeInventory(mode.getDefaultInventoryEncoded(), members.getPlayer());
                } else {
                    customInventory = SerializerUtils.deserializeInventory(data.getInventories().get(mode.getName()), members.getPlayer());
                }

                members.getPlayer().getInventory().setContents(customInventory.getContents());
            });
        });
    }

    @EventHandler
    public void onArenaEnd(ArenaEndEvent event) {

        //TODO: mostrar pós inventário aos jogadores aqui

        event.getArena().getAllTeamMembers().forEach(members -> {
            members.getPlayer().sendMessage("acabou a arena!!");
        });

        delay(() -> {

            event.getArena().getAllTeamMembers().forEach(members -> {

                SchedulerUtils.sync(() -> event.getArena().getGame().handleQuit(members));
            });

            if (event.getArena().getData() != null) {
                Practice.getInstance().getArenaStorage().unload(event.getArena().getId());
            } else {
                event.getArena().reset();
            }

        }, 4);
    }

    @EventHandler
    public void onUserDeath(UserDeathEvent event) {

        sync(() -> {

            EntityPlayer entityPlayer = ((CraftPlayer) event.getDead().getPlayer()).getHandle();

            double x = -Math.sin(Math.toRadians(event.getDead().getPlayer().getLocation().getYaw())) * 1.5;
            double y = 0.35;
            double z = Math.cos(Math.toRadians(event.getDead().getPlayer().getLocation().getYaw())) * 1.5;
            entityPlayer.motX = x;
            entityPlayer.motY = y;
            entityPlayer.motZ = z;

            PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity(entityPlayer.getId(), entityPlayer.motX, entityPlayer.motY, entityPlayer.motZ);
            ((CraftPlayer) event.getDead().getPlayer()).getHandle().playerConnection.sendPacket(velocityPacket);

            PacketPlayOutEntityStatus entityStatus = new PacketPlayOutEntityStatus(entityPlayer, (byte) 3);

            Bukkit.getOnlinePlayers().forEach(Other -> {
                if (Other != event.getDead().getPlayer()) {
                    ((CraftPlayer) Other).getHandle().playerConnection.sendPacket(entityStatus);
                }
            });

            event.getDead().getPlayer().setFireTicks(0);
            event.getDead().getPlayer().setAllowFlight(true);
            event.getDead().getPlayer().setFlying(true);

            event.getDead().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 5, false));
            event.getDead().getPlayer().playSound(event.getDead().getPlayer().getLocation(), Sound.ZOMBIE_UNFECT, 3.5F, 3.5F);

        });

        //TODO: CONFIGURAR MODO SPECTADOR AQUI!

        if (event.getKiller() == null) {

            event.getArena().getAllTeamMembers().forEach(members -> {
                members.getPlayer().sendMessage(event.getDead().getName() + " morreu.");
            });

        } else {

            event.getArena().getAllTeamMembers().forEach(members -> {
                members.getPlayer().sendMessage(event.getDead().getPlayer().getName() + " foi morto por " + event.getKiller().getPlayer().getName());
            });
        }

        User dead = event.getDead();
        Location deadLocation = event.getDead().getPlayer().getLocation();

        //TODO: colocar efeito de morte personalizado futuramente
        dead.getPlayer().getWorld().strikeLightningEffect(deadLocation);

        event.getDead().getTeam().addDeadMember(event.getDead());

        if (event.isLastMember()) {

            event.getArena().setStage(ArenaStage.ENDING);
            Bukkit.getServer().getPluginManager().callEvent(new ArenaChangeStateEvent(event.getArena(), event.getArena().getStage()));
        }
    }
}