package br.com.core.account;

import br.com.core.crud.redis.AccountRedisCRUD;
import br.com.core.data.AccountData;
import lombok.Data;

import java.util.UUID;

@Data
public class Account {
    private AccountData data;

    public String getName(){
        return getData().getName();
    }

    public UUID getUuid() {
        return getData().getUuid();
    }

    public Account(UUID uuid) {
        setData(new AccountData(uuid).createOrGetAccountData());
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
