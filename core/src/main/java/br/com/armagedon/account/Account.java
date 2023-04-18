package br.com.armagedon.account;

import br.com.armagedon.crud.redis.AccountRedisCRUD;
import br.com.armagedon.data.AccountData;
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
        data = new AccountData(uuid);
        setData(data.createDataOrGet(uuid));
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
