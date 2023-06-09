package br.com.core.data;

import br.com.core.Core;
import br.com.core.crud.redis.ServerRedisCRUD;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

@Data
public class ServerData {

    private Object _id = null;
    private String name = "...";
    private boolean online = false;
    private int currentPlayers = 0;

    public ServerData(){}

    public ServerData(String json) {

        ServerData serverData = Core.GSON.fromJson(json, ServerData.class);

        this._id = serverData.get_id();
        this.name = serverData.getName();
        this.online = serverData.isOnline();
        this.currentPlayers = serverData.getCurrentPlayers();
    }

    public void saveData() {
        ServerRedisCRUD.save(this);
    }

    public ServerData update() {
        return ServerRedisCRUD.findByName(getName());
    }

    public void delete() {
        ServerRedisCRUD.delete(getName());
    }

    public String toJson() {
        return Core.GSON.toJson(this);
    }

    public static ServerData fromJson(String json) {
        return Core.GSON.fromJson(json, ServerData.class);
    }

}
