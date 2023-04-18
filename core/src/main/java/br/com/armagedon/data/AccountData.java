package br.com.armagedon.data;

import br.com.armagedon.Core;
import br.com.armagedon.crud.redis.UserRedisCRUD;
import br.com.armagedon.utils.JsonUtils;
import com.google.gson.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;


@Data
@NoArgsConstructor
public class UserData {
    private Object _id;
    private String name;
    private UUID uuid;
    private int blocks;

    public UserData setDefaultData(String name, UUID uuid){
        return new UserData(name, uuid,0);
    }

    private UserData(String name, UUID uuid, int blocks) {
        this.name = name;
        this.uuid = uuid;
        this.blocks = blocks;
    }

    public UserData(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String fieldName = entry.getKey();
            JsonElement value = entry.getValue();

            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                JsonUtils.setFieldFromJson(field, this, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveData(){
        UserRedisCRUD.save(this);
    }

    public void deleteData() {
        UserRedisCRUD.delete(this.getUuid());
    }

    public String toJson() {
        return Core.GSON.toJson(this);
    }

    public static UserData fromJson(String json) {
        return Core.GSON.fromJson(json, UserData.class);
    }
}
