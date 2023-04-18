package br.com.armagedon.user;

import br.com.armagedon.Hub;
import br.com.armagedon.account.Account;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
public class User {
    private Account account;
    public User(UUID uuid) {
        account = new Account(uuid);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(account, user.account);
    }
}
