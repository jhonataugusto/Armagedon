package br.com.practice.user.storage;

import br.com.practice.user.User;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class UserStorage {

    public HashMap<UUID, User> users = new HashMap<>();

    public void register(UUID uuid, User user) {
        getUsers().put(uuid, user);
    }

    public void unregister(UUID uuid) {
        getUsers().remove(uuid);
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