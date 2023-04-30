package br.com.practice.listener;

import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import br.com.practice.arena.team.ArenaTeam;
import br.com.practice.events.arena.state.ArenaChangeStateEvent;
import br.com.practice.events.arena.state.ArenaEndEvent;
import br.com.practice.events.arena.pulse.ArenaPulseEvent;
import br.com.practice.events.arena.state.ArenaStartEvent;
import br.com.practice.events.arena.statistic.ArenaWinEvent;
import br.com.practice.events.user.UserExitArenaBoundsEvent;
import br.com.practice.events.user.UserDeathEvent;
import br.com.practice.user.User;
import br.com.practice.util.cuboid.Cuboid;
import br.com.practice.util.math.MathUtils;
import br.com.practice.util.rating.RatingUtil;
import br.com.practice.util.visibility.Visibility;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static br.com.practice.util.scheduler.SchedulerUtils.*;

public class ArenaListener implements Listener {

    private static final double DECIMAL_NERF_DAMAGE = 0.5;
    private static final double DECIMAL_NERF_KNOCKBACK = 2;

    @EventHandler(priority = EventPriority.MONITOR)
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

        if (damager.getClicksPerSecond() >= 16) {
            double newDamage = event.getDamage() - (event.getDamage() * DECIMAL_NERF_DAMAGE);
            event.setDamage(newDamage);
        }

        if (damager.getRange() >= 5) {
            double newDamage = event.getDamage() - (event.getDamage() * DECIMAL_NERF_DAMAGE * 2);
            event.setDamage(newDamage);
        }

        if (entity.getClicksPerSecond() >= 16) {
            double newDamage = event.getDamage() - (event.getDamage() * DECIMAL_NERF_DAMAGE);
            event.setDamage(newDamage);
        }

        damager.setRange(MathUtils.getEuclideanDistance(damager.getPlayer(), entity.getPlayer()));

        double finalDamage = event.getFinalDamage();
        double entityHealth = entity.getPlayer().getHealth();

        if (finalDamage >= entityHealth) {
            event.setCancelled(true);
            damager.getPlayer().setHealth(20);

            UserDeathEvent userDeathEvent = new UserDeathEvent(entity.getArena(), entity, damager, entity.isLastMember(), event);
            Practice.getInstance().getServer().getPluginManager().callEvent(userDeathEvent);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOtherDeathCauseEvents(EntityDamageEvent event) {
        Player entity = (Player) event.getEntity();
        EntityDamageEvent.DamageCause cause = event.getCause();
        double finalDamage = event.getFinalDamage();
        double entityHealth = entity.getHealth();

        if ((cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.FIRE_TICK) && finalDamage >= entityHealth) {
            event.setCancelled(true);

            User entityUser = User.fetch(entity.getUniqueId());

            if (entityUser == null) {
                return;
            }

            if (entityUser.getLastDamager() != null) {
                User lastDamager = entityUser.getLastDamager();
                lastDamager.getPlayer().setHealth(20);
                UserDeathEvent userDeathEvent = new UserDeathEvent(entityUser.getArena(), entityUser, lastDamager, entityUser.isLastMember());
                Practice.getInstance().getServer().getPluginManager().callEvent(userDeathEvent);
                return;
            }

            UserDeathEvent userDeathEvent = new UserDeathEvent(entityUser.getArena(), entityUser, null, entityUser.isLastMember());
            Practice.getInstance().getServer().getPluginManager().callEvent(userDeathEvent);
        }
    }

    @EventHandler
    public void onArenaPulse(ArenaPulseEvent event) {
        event.getArenas().forEach((id, arena) -> {
            arena.getAllTeamMembers().forEach(members -> {
                members.setClicksPerSecond(0);
            });
        });
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

        user.setClicksPerSecond(user.getClicksPerSecond() + 1);

        if (user.getClicksPerSecond() > user.getMaxClicksPerSecond()) {
            user.setMaxClicksPerSecond(user.getClicksPerSecond());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerDisconnect(PlayerQuitEvent event) {

        if (event.getPlayer() == null) {
            return;
        }

        User user = User.fetch(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        if (user.getArena() == null) {
            return;
        }

        Arena arena = user.getArena();

        UserDeathEvent userDeathEvent = new UserDeathEvent(arena, user, user.getLastDamager(), user.isLastMember());
        Practice.getInstance().getServer().getPluginManager().callEvent(userDeathEvent);

    }

    @EventHandler(priority = EventPriority.MONITOR)
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

    @EventHandler(priority = EventPriority.MONITOR)
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Location to = event.getTo();
            World world = to.getWorld();
            Block block = world.getBlockAt(to.getBlockX(), to.getBlockY(), to.getBlockZ());

            if (block.getType().isSolid()) {
                Location highestBlockLocation = world.getHighestBlockAt(to).getLocation();
                to.setX(highestBlockLocation.getX());
                to.setY(highestBlockLocation.getY());
                to.setZ(highestBlockLocation.getZ());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onUserExitBounds(UserExitArenaBoundsEvent event) {
        Practice.getInstance().getServer().getPluginManager().callEvent(new UserDeathEvent(event.getArena(), event.getUser(), event.getUser().getLastDamager(), event.getUser().isLastMember()));

        event.getUser().getPlayer().teleport(event.getArena().getMap().getArea().getCenter(event.getArena().getWorld()));
        event.getUser().getPlayer().sendMessage("Saiu da arena");
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onArenaStart(ArenaStartEvent event) {

        if (event.getArena() == null) {
            return;
        }

        Arena arena = event.getArena();

        event.getArena().getTeams().forEach(team -> {
            team.getMembers().forEach(members -> {
                arena.getGame().handleInventory(members);
            });
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
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

            arena.getData().getSpectators().forEach(uuid -> {
                User user = User.fetch(uuid);
                user.getArena().getGame().handleQuit(user);
            });

            if (arena.getData().isCustom()) {
                Practice.getInstance().getArenaStorage().unload(arena.getId());
            } else {
                arena.reset();
            }

        }, 4);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUserDeath(UserDeathEvent event) {

        if (event.getArena() == null || event.getDead().getTeam() == null) {
            return;
        }

        User dead = event.getDead();
        Arena arena = event.getArena();

        arena.getGame().handlePostMatchInventory(dead);

        if (event.getKiller() != null) {

            EntityPlayer entityPlayer = ((CraftPlayer) dead.getPlayer()).getHandle();

            double x = -Math.sin(Math.toRadians(event.getKiller().getPlayer().getLocation().getYaw()));
            double y = 0.35;
            double z = Math.cos(Math.toRadians(event.getKiller().getPlayer().getLocation().getYaw()));
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

        }

        dead.getPlayer().setFireTicks(0);
        dead.getPlayer().setAllowFlight(true);

        dead.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 5, false));
        dead.getPlayer().playSound(event.getDead().getPlayer().getLocation(), Sound.ZOMBIE_UNFECT, 3.5F, 3.5F);

        delay(() -> {
            Visibility.invisible(dead.getPlayer(), dead.getArena().getAllTeamMembers());
        }, 1);

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

            ArenaTeam winnersTeam;
            if (event.getKiller() == null) {
                winnersTeam = event.getDead().getTeam().getOpponent();
            } else {
                winnersTeam = event.getKiller().getTeam();
            }

            ArenaTeam losersTeam = event.getDead().getTeam();

            ArenaWinEvent arenaWinEvent = new ArenaWinEvent(arena, winnersTeam, losersTeam);
            Bukkit.getServer().getPluginManager().callEvent(arenaWinEvent);

            arena.setStage(ArenaStage.ENDING);
            Bukkit.getServer().getPluginManager().callEvent(new ArenaChangeStateEvent(arena, arena.getStage()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArenaWin(ArenaWinEvent event) {

        if (event.getArena() == null) {
            return;
        }

        Arena arena = event.getArena();

        if (event.getWinnerTeam() == null || event.getLoserTeam() == null) {
            return;
        }

        if (arena.getData().isRanked()) {
            int oldRatingWinnerTeam = event.getWinnerTeam().getAverageRating();
            int oldRatingLoserTeam = event.getLoserTeam().getAverageRating();

            int newRatingWinnerTeam = RatingUtil.calculateRating(event.getWinnerTeam().getAverageRating(), event.getLoserTeam().getAverageRating(), 1);
            int newRatingLoserTeam = RatingUtil.calculateRating(event.getLoserTeam().getAverageRating(), event.getWinnerTeam().getAverageRating(), 0);

            async(() -> {
                event.getWinnerTeam().setAverageRating(newRatingWinnerTeam);
                event.getLoserTeam().setAverageRating(newRatingLoserTeam);
            });

            int differenceRatingWinnerTeam = newRatingWinnerTeam - oldRatingWinnerTeam;
            int differenceRatingLoserTeam = oldRatingLoserTeam - newRatingLoserTeam;

            event.getWinnerTeam().getAllMembers().forEach(member -> member.getPlayer().sendMessage(ChatColor.GREEN + "+" + differenceRatingWinnerTeam));
            event.getLoserTeam().getAllMembers().forEach(member -> member.getPlayer().sendMessage(ChatColor.RED + "-" + differenceRatingLoserTeam));
        }
    }
}