package br.com.practice.listener;

import br.com.core.account.enums.preferences.Preference;
import br.com.core.account.enums.preferences.type.PreferenceType;
import br.com.core.data.DuelData;
import br.com.practice.Practice;
import br.com.practice.arena.Arena;
import br.com.practice.arena.stage.ArenaStage;
import br.com.practice.arena.team.ArenaTeam;
import br.com.practice.events.arena.state.ArenaChangeStateEvent;
import br.com.practice.events.arena.state.ArenaEndEvent;
import br.com.practice.events.arena.pulse.ArenaPulseEvent;
import br.com.practice.events.arena.state.ArenaStartEvent;
import br.com.practice.events.arena.statistic.ArenaWinEvent;
import br.com.practice.events.spectator.SpectatorLeaveArenaEvent;
import br.com.practice.events.user.UserExitArenaBoundsEvent;
import br.com.practice.events.user.UserDeathEvent;
import br.com.practice.user.User;
import br.com.practice.user.death.DeathStyle;
import br.com.practice.util.cuboid.Cuboid;
import br.com.practice.util.math.MathUtils;
import br.com.practice.util.rating.RatingUtil;
import br.com.practice.util.tag.TagUtil;
import br.com.practice.util.visibility.Visibility;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

import static br.com.practice.util.scheduler.SchedulerUtils.*;

public class ArenaListener implements Listener {

    private final int HIT_BLOCK_LIMIT = 30;
    private final int HIT_CRITICAL_LIMIT = 50;

    private static long REMOVE_UNUSED_ARENAS_DELAY = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(5);

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

        if (damager.getClicksPerSecond() >= 16) {
            double reductionFactor = 0.8;
            double newCpsDamage = event.getDamage() - (event.getDamage() * reductionFactor);
            event.setDamage(newCpsDamage);
        }

        if (damager.getRange() >= 3.2) {
            double reductionFactor = 1;
            double newRangeDamage = event.getDamage() - (event.getDamage() * reductionFactor);
            event.setDamage(newRangeDamage);
        }

        damager.setRange(MathUtils.getEuclideanDistance(damager.getPlayer(), entity.getPlayer()));
        damager.setSumOfRanges(damager.getSumOfRanges() + damager.getRange());

        if (entity.getPlayer().isBlocking() && entity.getBlockedHits() >= HIT_BLOCK_LIMIT) {
            double newBlockedHitDamage = event.getDamage() * 2;
            event.setDamage(newBlockedHitDamage);
        } else if (entity.getPlayer().isBlocking()) {
            entity.setBlockedHits(entity.getBlockedHits() + 1);
        }

        boolean criticalDamage = damager.getPlayer().getFallDistance() > 0 && !damager.getPlayer().isOnGround();

        if (criticalDamage && damager.getCriticalHits() >= HIT_CRITICAL_LIMIT) {
            double newCriticalDamage = event.getDamage() / 1.5F;
            event.setDamage(newCriticalDamage);
        } else if (criticalDamage) {
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

        User entityUser = User.fetch(entity.getUniqueId());

        if (entityUser == null) {
            return;
        }


        if (entityUser.getArena().getStage().equals(ArenaStage.STARTING) || entityUser.getArena().getStage().equals(ArenaStage.ENDING)) {
            event.setCancelled(true);
        }

        if ((cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.FIRE_TICK) && finalDamage >= entityHealth) {
            event.setCancelled(true);


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
            arena.getGameScoreboard().updateScoreboard();

            arena.getAllTeamMembers().forEach(members -> {
                members.setClicksPerSecond(0);
            });
        });

        if (System.currentTimeMillis() > REMOVE_UNUSED_ARENAS_DELAY) {
            Practice.getInstance().getArenaStorage().unloadUnusedArenaMaps();
            REMOVE_UNUSED_ARENAS_DELAY = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(5);
        }
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();
        world.setKeepSpawnInMemory(false);
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

        TagUtil.unloadTag(user.getPlayer());

        if (user.getArena().getCurrentSpectators().contains(user)) {
            Practice.getInstance().getServer().getPluginManager().callEvent(new SpectatorLeaveArenaEvent(arena, user));
            return;
        }

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
                int amplifier = 5;

                new BukkitRunnable() {
                    int seconds = 5;

                    @Override
                    public void run() {
                        for (User members : event.getArena().getAllTeamMembers()) {

                            if (members == null) continue;

                            if (seconds == 5) {
                                event.getArena().getGame().handleInventory(members);
                            }

                            if (seconds != 0) {
                                members.getPlayer().playSound(members.getPlayer().getLocation(), Sound.CLICK, 3.5F, 3.5F);
                                members.getPlayer().sendMessage("Iniciando o combate em " + seconds + " segundos.");
                            } else {
                                members.getPlayer().setWalkSpeed(0.2f);
                                members.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                                members.getPlayer().playSound(members.getPlayer().getLocation(), Sound.NOTE_PLING, 3F, 3F);
                                members.getPlayer().sendMessage("O duelo começou!");
                            }
                        }

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

                if (team1.getAliveMembers().size() == 0) {
                    Bukkit.getServer().getPluginManager().callEvent(new ArenaEndEvent(event.getArena(), team2, team1));

                    team2.getAliveMembers().forEach(members -> {

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

        if (event.getArena().getStage() == ArenaStage.PLAYING) {
            Practice.getInstance().getServer().getPluginManager().callEvent(new UserDeathEvent(event.getArena(), event.getUser(), event.getUser().getLastDamager(), event.getUser().isLastMember()));
            event.getUser().getPlayer().teleport(event.getArena().getMap().getArea().getTeamSpawn1());
        } else if (event.getArena().getStage() == ArenaStage.ENDING) {
            event.getUser().getPlayer().teleport(event.getArena().getMap().getArea().getTeamSpawn1());
        }

    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onArenaStart(ArenaStartEvent event) {
//        event.getArena().getTeams().forEach(team -> {
//            team.getAllMembers().forEach(members -> {
//                event.getArena().getGame().handleInventory(members);
//            });
//        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArenaEnd(ArenaEndEvent event) {

        if (event.getArena() == null) {
            return;
        }

        Arena arena = event.getArena();

        event.getArena().getAllTeamMembers().forEach(members -> {

            if (arena.getAllLiveMembers().contains(members)) {
                arena.getGame().handlePostMatchInventory(members);
            }

            members.getPlayer().getInventory().clear();
            members.getPlayer().getInventory().setArmorContents(null);

        });

        DuelData data = event.getArena().getData();

        for (Player player : event.getArena().getWorld().getPlayers()) {

            User user = User.fetch(player.getUniqueId());

            if (user == null) {
                continue;
            }

            if (data == null) {
                continue;
            }

            if (arena.is1v1()) {

                ArenaTeam team1 = arena.getTeams().get(0);
                ArenaTeam team2 = arena.getTeams().get(1);

                User user1 = team1.getAllMembers().stream().findFirst().orElse(null);
                User user2 = team2.getAllMembers().stream().findFirst().orElse(null);

                if (user1 == null || user2 == null) {
                    return;
                }

                TextComponent user1InventoryComponent = new TextComponent(user1.getTeam().getColor() + user1.getName());
                TextComponent user2InventoryComponent = new TextComponent(user2.getTeam().getColor() + user2.getName());

                TextComponent hoverTextInformationDuelComponent = new TextComponent("Clique para mais informações.");
                BaseComponent[] hoverBaseComponentTextInformationDuelComponent = new BaseComponent[]{hoverTextInformationDuelComponent};

                user1InventoryComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBaseComponentTextInformationDuelComponent));
                user2InventoryComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBaseComponentTextInformationDuelComponent));

                String commandToUser1 = "matchsolo " + data.getUuid() + " " + user1.getUuid();
                user1InventoryComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandToUser1));

                String commandToUser2 = "matchsolo " + data.getUuid() + " " + user2.getUuid();
                user2InventoryComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandToUser2));

                TextComponent matchInventory1v1Component = new TextComponent("");
                matchInventory1v1Component.addExtra(user1InventoryComponent);
                matchInventory1v1Component.addExtra("§r | ");
                matchInventory1v1Component.addExtra(user2InventoryComponent);

                BaseComponent[] finalBaseTextComponent = new BaseComponent[]{matchInventory1v1Component};

                player.sendMessage("");
                player.sendMessage(ChatColor.GREEN + "Estatísticas da partida:");
                player.spigot().sendMessage(finalBaseTextComponent);
                player.sendMessage("");

            } else {

                String command = "match " + data.getUuid();
                TextComponent component = new TextComponent(net.md_5.bungee.api.ChatColor.GREEN + "[Clique aqui para ver o resultado da partida]");
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));

                player.sendMessage("");
                player.sendMessage(component);
                player.sendMessage("");

            }

            user.getAccount().getData().setCurrentDuelContextUuid(null);
            user.getAccount().getData().saveData();
        }

        delay(() -> {

            arena.getAllTeamMembers().forEach(members -> {
                arena.getGame().handleQuit(members);
            });

            arena.getCurrentSpectators().forEach(spectators -> {
                arena.getGame().handleQuit(spectators);
            });

            arena.reset();

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

            for (User members : arena.getAllTeamMembers()) {
                if (members == null) continue;
                members.getPlayer().sendMessage(dead.getName() + " morreu.");
            }

        } else {

            for (User members : arena.getAllTeamMembers()) {
                if (members == null) continue;
                members.getPlayer().sendMessage(dead.getPlayer().getName() + " foi morto por " + event.getKiller().getPlayer().getName());
            }
        }

        PreferenceType type = PreferenceType.getByName(dead.getAccount().getData().getPreferenceByName(Preference.CUSTOM_DEATH).getType());

        DeathStyle deathStyle = DeathStyle.getByPreference(type);

        if (deathStyle == null) {
            return;
        }

        async(() -> deathStyle.getExecutor().execute(dead.getPlayer()));

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

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
}