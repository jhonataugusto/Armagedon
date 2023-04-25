package br.com.practice.arena.team;

import br.com.practice.arena.Arena;
import br.com.practice.user.User;
import lombok.Data;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;

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
        if(getMembers().contains(user)) {
            getDeadMembers().add(user);
            getMembers().remove(user);
        }
    }

    public void clear() {
        getMembers().clear();
        getDeadMembers().clear();
    }
}
