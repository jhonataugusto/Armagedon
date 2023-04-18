package br.com.armagedon.user;

import br.com.armagedon.Hub;
import br.com.armagedon.account.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
public class QueueProperties {

    private Account account;

    public static QueueProperties fetch(UUID uuid) {
        return Hub.getInstance().getUserStorage().getUser(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueueProperties queueProperties = (QueueProperties) o;
        return Objects.equals(account, queueProperties.account);
    }
}
