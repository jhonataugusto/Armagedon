package br.com.armagedon.account.storage;

import br.com.armagedon.Hub;
import br.com.armagedon.account.Account;
import br.com.armagedon.crud.redis.UserRedisCRUD;
import br.com.armagedon.data.UserData;

import java.util.HashMap;
import java.util.UUID;

public class AccountStorage {

    public HashMap<UUID, Account> users = new HashMap<>();

    public void register(UUID uuid, Account account) {
        getUsers().put(uuid, account);
    }

    public void unregister(UUID uuid) {
        getUsers().remove(uuid);
        UserData userData = UserRedisCRUD.findByUuid(uuid);
        if(userData != null) {
            Hub.getInstance().getUserMongoCRUD().save(userData);
        }
        Hub.getInstance().getUserRedisCRUD().delete(uuid);
    }

    public Account getUser(UUID uuid) {
        return getUsers().get(uuid);
    }


    public HashMap<UUID, Account> getUsers() {
        return users;
    }

    public void setUsers(HashMap<UUID, Account> users) {
        this.users = users;
    }
}
