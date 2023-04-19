package br.com.armagedon.account.storage;

import br.com.armagedon.account.Account;
import br.com.armagedon.crud.mongo.AccountMongoCRUD;
import br.com.armagedon.crud.redis.AccountRedisCRUD;
import br.com.armagedon.data.AccountData;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class AccountStorage {
    public HashMap<UUID, Account> accounts = new HashMap<>();
    public void register(UUID uuid, Account account) {
        getAccounts().put(uuid, account);
    }

    public void unregister(UUID uuid) {
        getAccounts().remove(uuid);
        AccountData accountData = AccountRedisCRUD.findByUuid(uuid);
        if(accountData != null) {
            AccountMongoCRUD.save(accountData);
        }
        AccountRedisCRUD.delete(uuid);
    }

    public Account getAccount(UUID uuid) {
        return getAccounts().get(uuid);
    }


    public HashMap<UUID, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(HashMap<UUID, Account> accounts) {
        this.accounts = accounts;
    }
}
