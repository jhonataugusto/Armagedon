package br.com.hub.user;

import br.com.hub.Hub;
import br.com.core.account.Account;
import br.com.hub.lobby.Lobby;
import br.com.hub.user.request.DuelRequest;
import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
public class User {
    private Account account;
    private Player player;
    private Lobby lobby;
    private List<DuelRequest> duelRequests;
    private JPerPlayerScoreboard scoreboard;

    public User(UUID uuid) {
        account = new Account(uuid);
        player = Bukkit.getPlayer(uuid);
        duelRequests = new ArrayList<>();
    }

    public String getName() {
        return getAccount().getName();
    }

    public UUID getUuid() {
        return getAccount().getUuid();
    }

    public static User fetch(UUID uuid) {
        return Hub.getInstance().getUserStorage().getUser(uuid);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getAccount().getUuid());
    }

    public DuelRequest getRequestById(String id) {
        return getDuelRequests().stream().filter(duelRequest -> duelRequest.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(account, user.account);
    }
}
