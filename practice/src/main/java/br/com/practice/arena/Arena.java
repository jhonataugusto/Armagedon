package br.com.practice.arena;

import br.com.core.account.Account;
import br.com.practice.arena.map.ArenaMap;
import br.com.practice.arena.stage.ArenaStage;
import br.com.practice.arena.team.ArenaTeam;
import br.com.core.data.DuelContextData;
import br.com.practice.events.arena.ArenaChangeStateEvent;
import br.com.practice.game.Game;
import br.com.practice.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
@Setter
public class Arena {
    private final long maxTime = TimeUnit.MINUTES.toMillis(5);
    private long currentTime = TimeUnit.MINUTES.toMillis(0);
    private DuelContextData data = null;
    private ArenaStage stage = ArenaStage.WAITING;
    private Game game;
    private World world;
    private ArenaMap map;
    private ArenaTeam[] teams;

    public Arena(Game game, World world, ArenaMap arenaMap) {
        this.game = game;
        this.world = world;
        this.map = arenaMap;

        ArenaTeam redTeam = new ArenaTeam("Vermelho", ChatColor.RED, this);
        ArenaTeam blueTeam = new ArenaTeam("Azul", ChatColor.BLUE, this);

        this.teams = new ArenaTeam[]{redTeam, blueTeam};
    }

    public Arena(Game game, World world, ArenaMap arenaMap, DuelContextData data) {
        this.game = game;
        this.world = world;
        this.map = arenaMap;

        ArenaTeam redTeam = new ArenaTeam("Vermelho", ChatColor.RED, this);
        ArenaTeam blueTeam = new ArenaTeam("Azul", ChatColor.BLUE, this);

        this.teams = new ArenaTeam[]{redTeam, blueTeam};
        this.data = data;

        teams[0].setOpponent(teams[1]);
        teams[1].setOpponent(teams[0]);
    }

    public String getDisplayArenaId() {
        return getGame().getMode() + "-" + getMap().getId();
    }

    public void handleJoin(User user) {

        Account account = Account.fetch(user.getUuid());

        if (!(getData().getTeam1().contains(account.getUuid()) || getData().getTeam2().contains(account.getUuid()))) {
            user.getPlayer().kickPlayer(ChatColor.RED + "Jogador não faz parte dessa partida");
            return;
        }

        if (getData().getTeam1().contains(account.getUuid())) {
            getTeams().get(0).add(user);
        } else {
            getTeams().get(1).add(user);
        }

        Location locationTeam1 = new Location(getWorld(), getMap().getArea().getTeam1X(), getMap().getArea().getTeam1Y(), getMap().getArea().getTeam1Z(), (int) getMap().getArea().getTeam1Pitch(), (int) getMap().getArea().getTeam1Yaw());
        Location locationTeam2 = new Location(getWorld(), getMap().getArea().getTeam2X(), getMap().getArea().getTeam2Y(), getMap().getArea().getTeam2Z(), (int) getMap().getArea().getTeam2Pitch(), (int) getMap().getArea().getTeam2Yaw());

        if (getTeams().get(0).getMembers().contains(user)) {
            user.getPlayer().teleport(locationTeam1);
        } else {
            user.getPlayer().teleport(locationTeam2);
        }

        user.setArena(this);

        int maxUsersExpected = getData().getTeam1().size() + getData().getTeam2().size();
        int actualUsers = getTeams().get(0).getMembers().size() + getTeams().get(1).getMembers().size();

        setStage(actualUsers == maxUsersExpected ? ArenaStage.STARTING : ArenaStage.WAITING);
        if (getStage() == ArenaStage.STARTING) {
            Bukkit.getServer().getPluginManager().callEvent(new ArenaChangeStateEvent(this, this.getStage()));
        }

        game.handleJoin(user.getPlayer());
    }

    public void reset() {
        setData(null);
        setStage(ArenaStage.WAITING);
        setCurrentTime(0L);
        getTeams().forEach(ArenaTeam::clear);
    }

    public String getId() {
        return getMap().getId();
    }

    public List<ArenaTeam> getTeams() {
        return Arrays.asList(teams);
    }

    public List<User> getAllTeamMembers() {

        List<User> allMembers = getTeams().stream().flatMap(team -> team.getMembers().stream()).collect(Collectors.toList());
        List<User> allDeadMembers = getTeams().stream().flatMap(team -> team.getDeadMembers().stream()).collect(Collectors.toList());

        allMembers.addAll(allDeadMembers);

        return allMembers;
    }
}