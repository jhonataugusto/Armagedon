package br.com.armagedon.data;

import br.com.armagedon.Core;
import br.com.armagedon.crud.redis.ServerRedisCRUD;
import br.com.armagedon.utils.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

@Data
public class ServerData {

    private Object _id;
    private String name;
    private boolean online;
    private int currentPlayers;

    public ServerData(String name, boolean online, int currentPlayers) {
        this.name = name;
        this.online = online;
        this.currentPlayers = currentPlayers;
    }

    public ServerData(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();


        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String fieldName = entry.getKey();
            JsonElement value = entry.getValue();

            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                JsonUtils.setFieldFromJson(field, value, this);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(){
        ServerRedisCRUD.save(this);
    }

    public ServerData update() {
        return ServerRedisCRUD.findByName(getName());
    }

    public void delete(){
        ServerRedisCRUD.delete(getName());
    }

    public String toJson(){
        return Core.GSON.toJson(this);
    }
    public static ServerData fromJson(String json) {
        return Core.GSON.fromJson(json, ServerData.class);
    }

}
