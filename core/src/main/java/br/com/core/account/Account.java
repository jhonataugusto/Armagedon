package br.com.core.account;

import br.com.core.account.enums.rank.Rank;
import br.com.core.crud.redis.AccountRedisCRUD;
import br.com.core.data.AccountData;
import br.com.core.data.object.RankDAO;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class Account {
    private AccountData data;
    private String lastTellSenderUserName;

    public Account(UUID uuid) {
        setData(AccountData.fetch(uuid));
    }

    public String getName() {
        return getData().getName();
    }

    public UUID getUuid() {
        return getData().getUuid();
    }

    public Rank getRank() {
        return Rank.getByName(Objects.requireNonNull(getData().getRanks().stream().findFirst().orElse(null)).getName());
    }

    public void setRank(Rank rank, long expiration) {
        this.getData().getRanks().clear();
        getData().getRanks().add(new RankDAO(rank.getName(), expiration));
    }


    public AccountData getData() {
        return data;
    }

    public void setData(AccountData data) {
        this.data = data;
    }

    public static Account fetch(UUID uuid) {
        Account account = new Account(uuid);
        account.setData(AccountRedisCRUD.findByUuid(uuid));
        return account;
    }
}
