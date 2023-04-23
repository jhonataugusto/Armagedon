package br.com.armagedon.arena;

import br.com.armagedon.account.Account;
import br.com.armagedon.arena.map.ArenaMap;
import br.com.armagedon.arena.team.ArenaTeam;
import br.com.armagedon.data.DuelContextData;
import br.com.armagedon.events.arena.ArenaChangeStateEvent;
import br.com.armagedon.game.Game;
import br.com.armagedon.user.User;
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
    private final long MAX_TIME = TimeUnit.MINUTES.toMillis(5);
    private long TIME = TimeUnit.MINUTES.toMillis(0);
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

    public String getId() {
        return getMap().getId();
    }

    public List<ArenaTeam> getTeams() {
        return Arrays.asList(teams);
    }

    public List<User> getAllTeamMembers() {

        /* Aqui, flatMap é usado para transformar a lista de jogadores de cada equipe em um único fluxo
         * de jogadores. Em seguida, collect é usado para coletar esses jogadores em uma única lista.*/

        return getTeams().stream().flatMap(team -> team.getMembers().stream()).collect(Collectors.toList());
    }
}
