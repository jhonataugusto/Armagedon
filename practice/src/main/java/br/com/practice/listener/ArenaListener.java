package br.com.practice.listener;

import br.com.core.data.AccountData;
import br.com.core.enums.game.GameMode;
import br.com.core.utils.serialization.GenericSerialization;
import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import br.com.practice.arena.team.ArenaTeam;
import br.com.practice.events.arena.ArenaChangeStateEvent;
import br.com.practice.events.arena.ArenaEndEvent;
import br.com.practice.events.arena.ArenaStartEvent;
import br.com.practice.events.user.UserExitArenaBoundsEvent;
import br.com.practice.events.user.UserDeathEvent;
import br.com.practice.gui.PostMatchGUI;
import br.com.practice.user.User;
import br.com.practice.util.cuboid.Cuboid;
import br.com.practice.util.math.MathUtils;
import br.com.practice.util.scheduler.SchedulerUtils;
import br.com.practice.util.serializer.SerializerUtils;
import fr.minuskube.inv.SmartInventory;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static br.com.practice.util.scheduler.SchedulerUtils.delay;
import static br.com.practice.util.scheduler.SchedulerUtils.sync;

public class ArenaListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamageArena(EntityDamageByEntityEvent event) {

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

        if (damager.getArena().getStage() == ArenaStage.STARTING || damager.getArena().getStage() == ArenaStage.ENDING || !entity.getArena().getAllTeamMembers().contains(damager)) {
            event.setCancelled(true);
            return;
        }

        if (entity.getLastDamager() == null) {
            entity.setLastDamager(damager);
        }

        if (damager.getRange() > damager.getMaxRange()) {
            damager.setMaxRange(damager.getRange());
        }

        damager.setRange(MathUtils.getEuclideanDistance(damager.getPlayer().getLocation(), entity.getPlayer().getLocation()));

        if (event.getFinalDamage() >= entity.getPlayer().getHealth()) {

            event.setCancelled(true);

            EntityDamageEvent.DamageCause cause = event.getCause();
            User lastDamager = entity.getLastDamager();

            UserDeathEvent userDeathEvent = new UserDeathEvent(entity.getArena(), entity, lastDamager, entity.isLastMember());

            if (cause == EntityDamageEvent.DamageCause.FIRE_TICK || cause == EntityDamageEvent.DamageCause.FALL) {
                Practice.getInstance().getServer().getPluginManager().callEvent(userDeathEvent);
            } else {
                Practice.getInstance().getServer().getPluginManager().callEvent(new UserDeathEvent(entity.getArena(), entity, damager, entity.isLastMember()));
            }
        }
    }

    @EventHandler
    public void onGetCPSPlayer(PlayerInteractEvent event) {

        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        Arena arena = user.getArena();

        if (arena == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long lastTime = user.getLastClickTime();
        double cps = 1000.0 / (currentTime - lastTime);

        user.setClicksPerSecond((int) cps);

        if (user.getClicksPerSecond() > user.getMaxClicksPerSecond()) {
            user.setMaxClicksPerSecond(user.getClicksPerSecond());
        }

        user.setLastClickTime(System.currentTimeMillis());
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

                ArenaTeam team1 = event.getArena().getTeams().get(0);
                ArenaTeam team2 = event.getArena().getTeams().get(1);

                if (team1.getMembers().size() == 0) {
                    Bukkit.getServer().getPluginManager().callEvent(new ArenaEndEvent(event.getArena(), team2, team1));

                    team2.getMembers().forEach(members -> {

                    });
                } else {
                    Bukkit.getServer().getPluginManager().callEvent(new ArenaEndEvent(event.getArena(), team1, team2));
                }
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
        Practice.getInstance().getServer().getPluginManager().callEvent(new UserDeathEvent(event.getArena(), event.getUser(), event.getUser().getLastDamager(), event.getUser().isLastMember()));

        event.getUser().getPlayer().teleport(event.getArena().getMap().getArea().getCenter(event.getArena().getWorld()));
        event.getUser().getPlayer().sendMessage("Saiu da arena");
    }


    @EventHandler
    public void onArenaStart(ArenaStartEvent event) {

        if(event.getArena() == null) {
            return;
        }

        Arena arena = event.getArena();

        event.getArena().getTeams().forEach(team -> {
            team.getMembers().forEach(members -> {
                arena.getGame().handleInventory(members);
            });
        });
    }

    @EventHandler
    public void onArenaEnd(ArenaEndEvent event) {

        if (event.getArena() == null) {
            return;
        }

        Arena arena = event.getArena();

        event.getArena().getAllTeamMembers().forEach(members -> {
            members.getPlayer().sendMessage("acabou a arena!!");

            if (arena.getAllLiveMembers().contains(members)) {
                arena.getGame().handlePostMatchInventory(members);
            }

            members.getPlayer().getInventory().clear();
            members.getPlayer().getInventory().setArmorContents(null);
        });

        delay(() -> {

            arena.getAllTeamMembers().forEach(members -> {
                arena.getGame().handleQuit(members);
            });

            if (arena.getData().isCustom()) {
                Practice.getInstance().getArenaStorage().unload(arena.getId());
            } else {
                arena.reset();
            }

        }, 4);
    }

    @EventHandler
    public void onUserDeath(UserDeathEvent event) {

        if (event.getArena() == null || event.getDead().getTeam() == null) {
            return;
        }

        User dead = event.getDead();
        Arena arena = event.getArena();

        arena.getGame().handlePostMatchInventory(dead);

        sync(() -> {

            EntityPlayer entityPlayer = ((CraftPlayer) dead.getPlayer()).getHandle();

            double x = -Math.sin(Math.toRadians(dead.getPlayer().getLocation().getYaw())) * 1.5;
            double y = 0.35;
            double z = Math.cos(Math.toRadians(dead.getPlayer().getLocation().getYaw())) * 1.5;
            entityPlayer.motX = x;
            entityPlayer.motY = y;
            entityPlayer.motZ = z;

            PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity(entityPlayer.getId(), entityPlayer.motX, entityPlayer.motY, entityPlayer.motZ);
            ((CraftPlayer) dead.getPlayer()).getHandle().playerConnection.sendPacket(velocityPacket);

            PacketPlayOutEntityStatus entityStatus = new PacketPlayOutEntityStatus(entityPlayer, (byte) 3);

            Bukkit.getOnlinePlayers().forEach(Other -> {
                if (Other != dead.getPlayer()) {
                    ((CraftPlayer) Other).getHandle().playerConnection.sendPacket(entityStatus);
                }
            });

            dead.getPlayer().setFireTicks(0);
            dead.getPlayer().setAllowFlight(true);
            dead.getPlayer().setFlying(true);

            dead.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 5, false));
            dead.getPlayer().playSound(event.getDead().getPlayer().getLocation(), Sound.ZOMBIE_UNFECT, 3.5F, 3.5F);

        });

        //TODO: CONFIGURAR MODO SPECTADOR AQUI!

        if (event.getKiller() == null) {

            arena.getAllTeamMembers().forEach(members -> {
                members.getPlayer().sendMessage(dead.getName() + " morreu.");
            });

        } else {

            arena.getAllTeamMembers().forEach(members -> {
                members.getPlayer().sendMessage(dead.getPlayer().getName() + " foi morto por " + event.getKiller().getPlayer().getName());
            });
        }

        Location deadLocation = event.getDead().getPlayer().getLocation();

        //TODO: colocar efeito de morte personalizado futuramente
        dead.getPlayer().getWorld().strikeLightningEffect(deadLocation);

        dead.getTeam().addDeadMember(event.getDead());

        if (event.isLastMember()) {

            arena.setStage(ArenaStage.ENDING);
            Bukkit.getServer().getPluginManager().callEvent(new ArenaChangeStateEvent(arena, arena.getStage()));
        }
    }
}