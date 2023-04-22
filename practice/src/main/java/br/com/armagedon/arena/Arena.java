package br.com.armagedon.arena;

import br.com.armagedon.Practice;
import br.com.armagedon.account.Account;
import br.com.armagedon.arena.map.ArenaMap;
import br.com.armagedon.arena.team.ArenaTeam;
import br.com.armagedon.data.DuelContextData;
import br.com.armagedon.game.Game;
import br.com.armagedon.user.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public void redirect(Player player) {

        Account account = Account.fetch(player.getUniqueId());

        //TODO: colocar no time certo

        if (!(getData().getTeam1().contains(account.getUuid()) || getData().getTeam2().contains(account.getUuid()))) {
            player.kickPlayer(ChatColor.RED + "Jogador não faz parte dessa partida");
            return;
        }

        User user = new User(account.getUuid());
        Practice.getInstance().getUserStorage().register(user.getUuid(), user);

        if (getData().getTeam1().contains(account.getUuid())) {
            getTeams().get(0).add(user);
        } else {
            getTeams().get(1).add(user);
        }

        //TODO: teleportar esse jogador para o canto certo do mapa

        if(getTeams().get(0).getMembers().contains(user)) {
            user.getPlayer().teleport(new Location(getWorld(), getMap().getArea().getTeam1X(), getMap().getArea().getTeam1Y(), getMap().getArea().getTeam1Z(), (int) getMap().getArea().getTeam1Pitch(), (int) getMap().getArea().getTeam1Yaw()));
        } else {
            user.getPlayer().teleport(new Location(getWorld(), getMap().getArea().getTeam2X(), getMap().getArea().getTeam2Y(), getMap().getArea().getTeam2Z(), (int) getMap().getArea().getTeam2Pitch(), (int) getMap().getArea().getTeam2Yaw()));
        }

        int maxUsersWaited = getData().getTeam1().size() + getData().getTeam2().size();
        int actualUsers = getTeams().get(0).getMembers().size() + getTeams().get(1).getMembers().size();

        setStage(actualUsers == maxUsersWaited ? ArenaStage.STARTING : ArenaStage.WAITING);

        game.handleJoin(player);
    }

    public String getId() {
        return getMap().getId();
    }

    public List<ArenaTeam> getTeams() {
        return Arrays.asList(teams);
    }
}
