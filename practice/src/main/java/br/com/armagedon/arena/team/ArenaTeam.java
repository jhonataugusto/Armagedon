package br.com.armagedon.arena.team;

import br.com.armagedon.arena.Arena;
import br.com.armagedon.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class ArenaTeam {
    private String name;
    private ChatColor color;
    private Set<User> members;
    private Arena arena;

    public ArenaTeam(String name, ChatColor color, Arena arena) {
        this.name = name;
        this.color = color;
        this.arena = arena;
    }

    public void add(User user) {
        getMembers().add(user);
    }

    public void remove(User user) {
        getMembers().remove(user);
    }

}
