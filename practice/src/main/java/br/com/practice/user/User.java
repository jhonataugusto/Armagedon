package br.com.practice.user;

import br.com.practice.Practice;
import br.com.core.account.Account;
import br.com.practice.arena.Arena;
import br.com.practice.arena.team.ArenaTeam;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
public class User {
    private Account account;

    private Player player;

    private Arena arena;

    private User lastDamager;

    public User(UUID uuid) {
        account = new Account(uuid);
        player = Bukkit.getPlayer(uuid);
    }

    public String getName() {
        return getAccount().getName();
    }

    public UUID getUuid() {
        return getAccount().getUuid();
    }

    public static User fetch(UUID uuid) {
        return Practice.getInstance().getUserStorage().getUser(uuid);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getAccount().getUuid());
    }

    public ArenaTeam getTeam() {
        return getArena().getTeams().stream().filter(team -> team.getMembers().contains(this)).findFirst().orElse(null);
    }

    //TODO: amanhã ver o bug do lastMember e checar o por que da conta do Tringed dar mais dano '-'
    public boolean lastMember() {

        List<User> members = getTeam().getMembers().stream()
                .map(member -> (User) member)
                .collect(Collectors.toList());

        return members.size() == 1 && members.contains(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(account, user.account);
    }
}
