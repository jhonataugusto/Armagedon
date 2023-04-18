package br.com.armagedon.account;

import br.com.armagedon.crud.mongo.AccountMongoCRUD;
import br.com.armagedon.crud.redis.AccountRedisCRUD;
import br.com.armagedon.data.AccountData;
import lombok.Data;

import java.util.UUID;

@Data
public class Account {
    private String name;
    private UUID uuid;
    private int blocks;
    private AccountData accountData;

    public Account(String name, UUID uuid) {
        setName(name);
        setUuid(uuid);
        setAccountData(createUserDataOrGet());
    }

    public AccountData getAccountData() {
        return accountData;
    }

    public void setAccountData(AccountData accountData) {
        this.accountData = accountData;
    }

    public AccountData createUserDataOrGet() {
        AccountData data = AccountRedisCRUD.findByUuid(this.getUuid());

        if (data == null) {
            data = AccountMongoCRUD.get(this.getUuid());

            if (data != null) {
                AccountRedisCRUD.save(data);
            }
        }

        if (data == null) {
            data = new AccountData().setDefaultData(getName(), getUuid());
            AccountRedisCRUD.save(data);
            AccountMongoCRUD.create(data);
        }

        return data;
    }


}
