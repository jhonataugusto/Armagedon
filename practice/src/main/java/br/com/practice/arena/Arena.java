package br.com.practice.arena;

import br.com.core.Core;
import br.com.core.crud.redis.DuelRedisCRUD;
import br.com.practice.arena.map.ArenaMap;
import br.com.practice.arena.stage.ArenaStage;
import br.com.practice.arena.team.ArenaTeam;
import br.com.core.data.DuelData;
import br.com.practice.events.arena.state.ArenaChangeStateEvent;
import br.com.practice.game.Game;
import br.com.practice.user.User;
import dev.jcsoftware.jscoreboards.JGlobalScoreboard;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static br.com.practice.util.scheduler.SchedulerUtils.sync;

@Getter
@Setter
public class Arena {
    private final long maxTime = TimeUnit.MINUTES.toMillis(5);
    private long currentTime = TimeUnit.MINUTES.toMillis(0);
    private DuelData data;
    private ArenaStage stage = ArenaStage.FREE;
    private Game game;
    private World world;
    private ArenaMap map;
    private ArenaTeam[] teams;
    private Set<User> currentSpectators;
    private JGlobalScoreboard gameScoreboard;

    public Arena(Game game, World world, ArenaMap arenaMap) {
        this.game = game;
        this.world = world;
        this.map = arenaMap;

        ArenaTeam redTeam = new ArenaTeam("Vermelho", ChatColor.RED, this);
        ArenaTeam blueTeam = new ArenaTeam("Azul", ChatColor.BLUE, this);

        this.teams = new ArenaTeam[]{redTeam, blueTeam};
        this.currentSpectators = new HashSet<>();

        teams[0].setOpponent(teams[1]);
        teams[1].setOpponent(teams[0]);

        createScoreboard();
    }

    public Arena(Game game, World world, ArenaMap arenaMap, DuelData data) {
        this.game = game;
        this.world = world;
        this.map = arenaMap;

        ArenaTeam redTeam = new ArenaTeam("Vermelho", ChatColor.RED, this);
        ArenaTeam blueTeam = new ArenaTeam("Azul", ChatColor.BLUE, this);

        this.teams = new ArenaTeam[]{redTeam, blueTeam};
        this.currentSpectators = new HashSet<>();
        this.data = data;

        teams[0].setOpponent(teams[1]);
        teams[1].setOpponent(teams[0]);

        createScoreboard();
    }

    public String getDisplayArenaId() {
        return getGame().getMode() + "-" + getMap().getId();
    }

    public void handleJoin(User user) {

        setStage(ArenaStage.WAITING);

        if (!(getData().getTeam1().contains(user.getUuid()) || getData().getTeam2().contains(user.getUuid()))) {
            user.getPlayer().kickPlayer(ChatColor.RED + "Jogador não faz parte dessa partida");
            return;
        }

        if (getData().getTeam1().contains(user.getUuid())) {
            getTeams().get(0).add(user);
        } else {
            getTeams().get(1).add(user);
        }

        Location locationTeam1 = new Location(getWorld(), getMap().getArea().getTeam1X(), getMap().getArea().getTeam1Y(), getMap().getArea().getTeam1Z(), (int) getMap().getArea().getTeam1Pitch(), (int) getMap().getArea().getTeam1Yaw());
        Location locationTeam2 = new Location(getWorld(), getMap().getArea().getTeam2X(), getMap().getArea().getTeam2Y(), getMap().getArea().getTeam2Z(), (int) getMap().getArea().getTeam2Pitch(), (int) getMap().getArea().getTeam2Yaw());

        if (getTeams().get(0).getAliveMembers().contains(user)) {
            user.getPlayer().teleport(locationTeam1);
            user.setTeam(getTeams().get(0));
        } else {
            user.getPlayer().teleport(locationTeam2);
            user.setTeam(getTeams().get(1));
        }

        user.setArena(this);

        int maxUsersExpected = getData().getTeam1().size() + getData().getTeam2().size();
        int actualUsers = getTeams().get(0).getAliveMembers().size() + getTeams().get(1).getAliveMembers().size();

        setStage(actualUsers >= maxUsersExpected ? ArenaStage.STARTING : ArenaStage.WAITING);

        Bukkit.getServer().getPluginManager().callEvent(new ArenaChangeStateEvent(this, this.getStage()));
        game.handleJoin(user.getPlayer());
    }

    public void updateScoreboard() {
        gameScoreboard.updateScoreboard();
    }

    public void handleScoreboard(User user) {

        if (user.getArena() == null) {
            return;
        }

        getGameScoreboard().addPlayer(user.getPlayer());
    }

    public void removeScoreboard(User user) {
        getGameScoreboard().removePlayer(user.getPlayer());
    }

    public void createScoreboard() {
        setGameScoreboard(new JGlobalScoreboard(
                () -> "§l" + Core.SERVER_NAME.toUpperCase(),

                () -> {

                    switch (getStage()) {
                        case FREE:
                            return Arrays.asList(
                                    "",
                                    ChatColor.GREEN + "ARENA LIVRE"
                                    , "id: " + ChatColor.GRAY + getMap().getId()
                            );

                        case WAITING:
                            return Arrays.asList(
                                    "",
                                    ChatColor.YELLOW + "AGUARDANDO"
                                    , ""
                            );

                        case STARTING:
                            return Arrays.asList(
                                    "",
                                    ChatColor.GREEN + "INICIANDO"
                                    , ""
                            );
                        case PLAYING:

                            if (is1v1()) {

                                ArenaTeam team1 = getTeams().get(0);
                                ArenaTeam team2 = getTeams().get(1);

                                User user1 = team1.getAllMembers().stream().findFirst().orElse(null);
                                User user2 = team2.getAllMembers().stream().findFirst().orElse(null);

                                if (user1 == null || user2 == null) {
                                    return Collections.singletonList(ChatColor.RED + "JOGADOR INEXISTENTE");
                                }

                                String gameModeName = user1.getArena().getGame().getMode().getName();

                                int user1Elo = user1.getAccount().getData().getEloByGameModeName(gameModeName).getElo();
                                int user2Elo = user2.getAccount().getData().getEloByGameModeName(gameModeName).getElo();

                                List<String> scoreboardContents = new ArrayList<>();

                                scoreboardContents.add("");
                                scoreboardContents.add("Mapa: " + ChatColor.GRAY + this.getMap().getName());
                                scoreboardContents.add("Arena: " + ChatColor.GRAY + this.getId());
                                scoreboardContents.add("");

                                scoreboardContents.add(user1.getTeam().getColor() + user1.getName());

                                if (getData().isRanked()) {
                                    scoreboardContents.add(user1.getTeam().getColor() + "ELO: §r" + user1Elo);
                                }

                                scoreboardContents.add(user1.getTeam().getColor() + "Ping: §r" + user1.getPing());

                                scoreboardContents.add("");

                                scoreboardContents.add(user2.getTeam().getColor() + user2.getName());

                                if (getData().isRanked()) {
                                    scoreboardContents.add(user2.getTeam().getColor() + "ELO: §r" + user2Elo);
                                }
                                scoreboardContents.add(user2.getTeam().getColor() + "Ping: §r" + user2.getPing());
                                scoreboardContents.add("");

                                if (getCurrentSpectators().size() > 0) {
                                    scoreboardContents.add("Assistindo: " + ChatColor.YELLOW + getCurrentSpectators().size());
                                    scoreboardContents.add("");
                                }

                                scoreboardContents.add(ChatColor.WHITE + Core.SERVER_WEBSITE);

                                return scoreboardContents;

                            } else {
                                Set<User> teamBlue = getTeams().get(0).getAliveMembers();
                                Set<User> teamRed = getTeams().get(1).getAliveMembers();
                                int avgPingBlue = calculateAveragePing(teamBlue);
                                int avgPingRed = calculateAveragePing(teamRed);

                                return Arrays.asList(
                                        "",
                                        "Mapa: " + ChatColor.GRAY + this.getMap().getName(),
                                        "Arena: " + ChatColor.GRAY + this.getDisplayArenaId(),
                                        "",
                                        ChatColor.YELLOW + "Party Team",
                                        "",
                                        ChatColor.BLUE + "Restantes: §r" + teamBlue.size(),
                                        ChatColor.BLUE + "Media Ping: §r" + avgPingBlue + "ms",
                                        "",
                                        ChatColor.RED + "Restantes: §r" + teamRed.size(),
                                        ChatColor.RED + "Media ping: §r" + avgPingRed + "ms",
                                        "",
                                        "Assistindo:" + getCurrentSpectators().size()
                                );
                            }

                        case ENDING:
                            return Arrays.asList(
                                    "",
                                    ChatColor.YELLOW + "FIM DE JOGO",
                                    ""
                            );

                        default:
                            return Arrays.asList(
                                    "",
                                    ChatColor.RED + "DESCONHECIDO",
                                    ""
                            );
                    }
                }
        ));
    }

    private int calculateAveragePing(Set<User> team) {

        int totalPing = 0;
        int count = 0;

        for (User user : team) {
            count++;
            EntityPlayer player = ((CraftPlayer) user.getPlayer()).getHandle();

            totalPing += player.ping;
        }

        if (count == 0) return 0;

        return totalPing / count;
    }

    public void reset() {
        sync(() -> {
            setStage(ArenaStage.RESETTING);

            //TODO: colocar rollback aqui

            setCurrentTime(0L);
            getTeams().forEach(ArenaTeam::clear);
            getWorld().getEntities().forEach(Entity::remove);
            getCurrentSpectators().clear();

            getWorld().save();

            if (getData() != null) {
                setData(null);
            }

            setStage(ArenaStage.FREE);
            Bukkit.getServer().getPluginManager().callEvent(new ArenaChangeStateEvent(this, this.getStage()));
        });
    }

    public String getId() {
        return getMap().getId();
    }

    public List<ArenaTeam> getTeams() {
        return Arrays.asList(teams);
    }

    public List<User> getAllTeamMembers() {

        List<User> allDeadMembers = getAllDeadMembers();
        List<User> allMembers = getAllLiveMembers();

        allMembers.addAll(allDeadMembers);

        return allMembers;
    }

    public List<User> getAllLiveMembers() {
        return getTeams().stream().flatMap(team -> team.getAliveMembers().stream()).collect(Collectors.toList());
    }

    public List<User> getAllDeadMembers() {
        return getTeams().stream().flatMap(team -> team.getDeadMembers().stream()).collect(Collectors.toList());
    }

    public boolean is1v1() {
        return (getTeams().get(0).getAllMembers().size() == 1) && (getTeams().get(1).getAllMembers().size() == 1);
    }
}
