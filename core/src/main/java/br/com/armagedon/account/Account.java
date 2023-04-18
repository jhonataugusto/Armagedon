package br.com.armagedon.user;

import br.com.armagedon.Hub;
import br.com.armagedon.data.UserData;
import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private String name;
    private UUID uuid;
    private int blocks;
    private UserData userData;

    public User(String name, UUID uuid) {
        setName(name);
        setUuid(uuid);
        setUserData(createUserDataOrGet());
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserData createUserDataOrGet() {
        UserData data = Hub.getInstance().getUserRedisCRUD().findByUuid(this.getUuid());

        if (data == null) {
            data = Hub.getInstance().getUserMongoCRUD().get(this.getUuid());

            if (data != null) {
                Hub.getInstance().getUserRedisCRUD().save(data);
            }
        }

        if (data == null) {
            data = new UserData().setDefaultData(getName(), getUuid());
            Hub.getInstance().getUserRedisCRUD().save(data);
            Hub.getInstance().getUserMongoCRUD().create(data);
        }

        return data;
    }

    public static User fetch(UUID uuid) {
        return Hub.getInstance().getUserStorage().getUser(uuid);
    }
}
