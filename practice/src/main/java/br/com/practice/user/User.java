package br.com.practice.user;

import br.com.practice.Practice;
import br.com.core.account.Account;
import br.com.practice.arena.Arena;
import br.com.practice.arena.team.ArenaTeam;
import br.com.practice.gui.statistics.PostMatchGUI;
import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
public class User {
    private Account account;
    private Player player;
    private Arena arena;
    private ArenaTeam team;
    private User lastDamager;
    private int blockedHits, hits, criticalHits;
    private int maxCombo, currentCombo;

    private int throwedPotions, missedPotions, stealedPotions, successfulPotions;
    private double sumAccuracyPotions, averageAccuracyPotions;

    private double range, sumOfRanges, averageRange;
    private double clicksPerSecond, maxClicksPerSecond;

    public User(UUID uuid, Player player) {
        account = new Account(uuid);
        this.player = player;
    }

    public String getName() {
        return getPlayer().getDisplayName();
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

    public int getPing(){
        return ((CraftPlayer) this.getPlayer()).getHandle().ping;
    }

    public boolean isLastMember() {

        List<User> members = getTeam().getAliveMembers().stream()
                .map(member -> (User) member)
                .collect(Collectors.toList());

        return members.size() == 1 && members.contains(this);
    }

    public boolean isSpectating(){
        if(this.getArena() == null) {
            return false;
        }

        return this.getArena().getCurrentSpectators().contains(this);
    }

    public Inventory createPostMatchInventory() {
        return new PostMatchGUI(this).getInventory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(account, user.account);
    }
}
