package br.com.armagedon.arena.team;

import br.com.armagedon.user.User;
import lombok.Data;

import java.util.Set;

@Data
public class ArenaTeam {
    private Set<User> members;
}
