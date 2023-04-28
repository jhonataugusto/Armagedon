package br.com.practice.arena.team;

import br.com.practice.arena.Arena;
import br.com.practice.user.User;
import lombok.Data;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ArenaTeam {
    private String name;
    private ChatColor color;
    private Set<User> members;
    private Set<User> deadMembers;
    private Arena arena;
    private ArenaTeam opponent;
    private boolean winner;

    public ArenaTeam(String name, ChatColor color, Arena arena) {
        this.name = name;
        this.color = color;
        this.arena = arena;
        this.members = new HashSet<>();
        this.deadMembers = new HashSet<>();
    }

    public void add(User user) {
        getMembers().add(user);
    }

    public void remove(User user) {
        getMembers().remove(user);
    }

    public void ressurectDeadMember(User user) {
        if (getDeadMembers().contains(user)) {
            getDeadMembers().remove(user);
            getMembers().add(user);
        }
    }

    public void addDeadMember(User user) {
        if (getMembers().contains(user)) {
            getDeadMembers().add(user);
            getMembers().remove(user);
        }
    }

    public void clear() {
        getMembers().clear();
        getDeadMembers().clear();
    }

    public Set<User> getAllMembers() {
        Set<User> allMembers = new HashSet<>(getMembers());
        allMembers.addAll(getDeadMembers());
        return allMembers;
    }
    public int getAverageRating() {
        int totalRating = 0;

        for (User user : getAllMembers()) {
            totalRating += user.getAccount().getData().getElo(getArena().getGame().getMode());
        }
        int totalMembers = getAllMembers().size();
        return totalRating / totalMembers;
    }

    public void setAverageRating(int newRating) {
        getAllMembers().forEach(member -> {
            String gameModeName = getArena().getGame().getMode().getName();
            member.getAccount().getData().getElo().replace(gameModeName, newRating);
            member.getAccount().getData().saveData();
        });
    }
}
