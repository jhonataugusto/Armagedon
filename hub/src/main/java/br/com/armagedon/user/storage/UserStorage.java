package br.com.armagedon.user.storage;

import br.com.armagedon.crud.mongo.AccountMongoCRUD;
import br.com.armagedon.crud.redis.AccountRedisCRUD;
import br.com.armagedon.data.AccountData;
import br.com.armagedon.user.User;

import java.util.HashMap;
import java.util.UUID;

public class UserStorage {

    public HashMap<UUID, User> users = new HashMap<>();

    public void register(UUID uuid, User user) {
        getUsers().put(uuid, user);
    }

    public void unregister(UUID uuid) {
        getUsers().remove(uuid);
        AccountData accountData = AccountRedisCRUD.findByUuid(uuid);
        if(accountData != null) {
            AccountMongoCRUD.save(accountData);
        }
        AccountRedisCRUD.delete(uuid);
    }

    public User getUser(UUID uuid) {
        return getUsers().get(uuid);
    }


    public HashMap<UUID, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<UUID, User> users) {
        this.users = users;
    }
}
